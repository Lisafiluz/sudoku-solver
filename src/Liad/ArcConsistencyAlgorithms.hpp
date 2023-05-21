#include "CSP_Graph.hpp"

namespace ArcConsistencyAlg{
    template<class T>
    Graph<Vertex<T>,int> AC3(Graph<Vertex<T>,int> G){
        queue<pair<int,int>> Q;
        set<pair<int,int>> S;  // chake if object in queue
        initQueue_Set(G, Q, S);
        
        while(!Q.empty()){
            pair<int,int>& arc = Q.top();    // arc is pair<int,int>&
            Q.pop();
            if (!arcConsistency(G, arc)){
                // return true iff the arc is consistent
                //  the function update the domain variable if needed
                InsertArcs(G, Q, S, arc);
            }
        }
    }

    template<class T>
    void initQueue_Set(const Graph<Vertex<T>,int>& G, queue<pair<int,int>>& Q, set<pair<int,int>>& S){
        for(auto& v_from : G.verticesIterator()){
            for(auto& e : G.edgesIterator()){
                int v_to = e->first;
                pair<int,int> p(v_from->first, v_to);   // p is free outside the function???
                Q.push(p);
                S.insert(p);
            }
        }
    }

    template<class T>
    bool arcConsistency(const Graph<Vertex<T>,int>& G, pair<int,int>& arc){
        VareiableVertex<T> V = G.getVertexData(p->first);
        ConstraintVertex<T> C = G.getEdgeData(p->second);
        set<T> delete_items_in_V;
        bool ret = true;
        for(auto& x : V.domain()){ // need to implement domain iterator
            // x is value in type T
            for(auto& U : C.Variables(V, x)){ // need to implement variables iterator for all the options, fix V=x
                // U is an assignment
                if (C.con.check_constraint(U)){ // check if the assignment satisfy the constraint
                    break;
                }
            }
            // x is inconsistent
            ret = false;
            delete_items_in_V.insert(x);
        }
        for(auto& x : delete_items_in_V){
            V.deleteValue(x);
        }

        return ret;
    }

    template<class T>
    void InsertArcs(const Graph<Vertex<T>,int>& G, queue<pair<int,int>>& Q, set<pair<int,int>>& S, pair<int,int>& arc){
        int v_from = arc->first;
        for(auto& e : G.edgesIterator(v_from)){
            int v_to = e->first;
            if(v_to != p->second){   // all the constraint that is not the current constraint to p
                ConstraintVertex<T> c = G.getVertexData(v_to);
                for(auto& v: c.con_vertices){   // v is VariableVertex
                // if v,v_from is not the arc p and if the inserted arc is not already in Q (and S)
                    if(v.v_id != v_from && S.find(pair<int,int>(v.v_id, v_to)!=S.end())){
                        // insert
                        S.insert(pair<int,int>(v.v_id, v_to));
                        Q.push(pair<int,int>(v.v_id, v_to));
                    }

                }

            }
        }
    }
};