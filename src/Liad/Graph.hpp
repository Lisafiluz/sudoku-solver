#include <iostream>
#include <list>
#include <vector>
#include <unordered_map>
#include <iterator> // For std::forward_iterator_tag

using namespace std;

// VD is additinal optinal data for vertices
// ED is additinal optinal data for edges
template<class VD = int, class ED = int>
class Graph{
    // This is a generic iterator over unordered_map<int, T> object
    template <class T>
    class GraphIterator{
        public:
        GraphIterator(unordered_map<int, T>& m){
            this->m = m;
        }
        
        // Using code from: https://www.internalpointers.com/post/writing-custom-iterators-modern-cpp

        using value_type        = typename unordered_map<int,T>::iterator;
        struct Iterator{
            using iterator_category = forward_iterator_tag;
            using difference_type   = ptrdiff_t;
            using pointer           = value_type*;
            using reference         = value_type&;
            
            Iterator(pointer ptr) : m_ptr(ptr){}
            
            reference operator*() const { return *m_ptr; }
            pointer operator->() { return m_ptr; }

            // Prefix increment
            Iterator& operator++() { 
                (*m_ptr)++; // increment unordered_map iterator object
                return *this; 
            }  

            // Postfix increment
            Iterator operator++(int) { 
                Iterator tmp = *this; 
                ++(*this);
                return tmp; 
            }

            friend bool operator== (const Iterator& a, const Iterator& b) { 
                return *(a.m_ptr) == *(b.m_ptr);    // compare between unorderes_map iterator objects
            };
            friend bool operator!= (const Iterator& a, const Iterator& b) { 
                return *(a.m_ptr) != *(b.m_ptr);    // compare between unorderes_map iterator objects
            };     
            
            private:
                pointer m_ptr;
        };
        Iterator begin() { 
            beg_iterator = m.begin();
            return Iterator(&beg_iterator);
        }
        Iterator end()   {
            end_iterator = m.end();
            return Iterator(&end_iterator); 
        } 
        private:
            unordered_map<int, T> m;
            value_type beg_iterator;
            value_type end_iterator;
    };

    using VertexList = unordered_map<int, VD>;
    using VertexListRef = VertexList&;
    using EdgesList = unordered_map<int, ED>;
    using EdgesListRef = EdgesList&;
    using AdjList = unordered_map<int, EdgesList>;
    using AdjListRef = AdjList&;

    private:
        VertexList vertices;    // Each vertex has a unique integer key
        AdjList edges;          // Adjancency List representation:
                                //  in each int key there is the adjancency list of the
                                //  neighbors of the corresponded vertex. the list contains
                                //  [key of vertex, data of the edge]
        bool directed;

        // // Get properties for Iterators only
        // VertexListRef getVertices(){ return vertices; }
        // AdjListRef getEdges(){ return edges; }
        // VertexListRef getEdgesOutOfVertex(int v_from_id){ return edges[v_from_id]; }
        
    public:
        // Constructors & Destructors
        Graph(bool dir = false){    // Empty const. 
        // dir - OPTIANAL. true if the graph is directed and false otherwise
             cout << "#Empty Constructor call on: " << this << "#" << endl;
             directed = dir;
        }   
        Graph(const Graph& copy_g){ // Copy const. TODO
            cout << "#Copy Constructor call on: " << this << "#" << endl;
        }
        Graph(const int* ver_ids, int s_ver, const vector<int>* edges_by_ids, int s_edg,
             const VD* ver_data = 0, const ED* edges_data = 0, bool dir = false){
            // const int* ver_ids - a list of vertices_id to insert the graph
            // int s_ver - size of ver_ids list
            // const vector<int>* edges_by_ids - a list of edges to insert the graph. 
            //      Each element is an edge by id: (v_from_id, v_to_id)
            // int s_edg - size of edges list.
            // const VD* ver_data - OPTIANAL. a list of corresponded data of the vertices in ver_ids.
            // const ED* edges_data - OPTIANAL. a list of corresponded data of the edges in edges_by_ids.
            // dir - OPTIANAL. true if the graph is directed and false otherwise
            directed = dir;

            if(ver_data == 0)
                for(int i = 0; i < s_ver; i++)
                    insertVertex(*(ver_ids+i));
            else
                for(int i = 0; i < s_ver; i++)
                    insertVertex(*(ver_ids+i), *(ver_data+i));

            if(edges_data == 0)
                for(int i = 0; i < s_edg; i++)
                    insertEdge(*(edges_by_ids+i));
            else
                for(int i = 0; i < s_edg; i++)
                    insertEdge(*(edges_by_ids+i), *(edges_data+i));
                
        }
        ~Graph(){                   // destr.
            cout << "#Destrctor call on: " << this << "#" << endl;
        }
        
        // Iterators
        // TODO (?): The user can change the graph properties through the iterator
        GraphIterator<VD> verticesIterator(){
            // Typical using:
            // for(auto& v : g.verticesIterator()){ // g is instance of Graph
            //     cout << v->first << ":" << v->second << endl; // v_id:v_data
            // }
            return GraphIterator(vertices);
        }
        GraphIterator<ED> edgesIterator(int v_from_id){
            // Typical using:
            // Graph<> g ...
            // int v ...
            // for(auto& e : g.edgesIterator(v)){ // g is instance of Graph, v is valid vertex id in g
            //     cout << "(" << v << "," << e->first << ") = " << e->second << endl; // (v_id_from, v_id_to) = e_data
            // }
            return GraphIterator(edges[v_from_id]);
        }

        // Getters & Setters
        bool isDirected(){ return directed; }
        VD& getVertexData(int v_id) { return vertices[v_id]; }
        ED& getEdgeData(int v_from_id, int v_to_id){
            return edges[v_from_id][v_to_id];
        }
        ED& getEdgeData(const vector<int>& e){
            return edges[e[0]][e[1]];
        }
        void setVertexData(int v_id, VD& new_data){
            vertices[v_id] = VD(new_data);
        }
        void setEdgeData(int v_from_id, int v_to_id, ED& new_data){
            edges[v_from_id][v_to_id] = ED(new_data);
        }
        void setEdgeData(const vector<int>& e, ED& new_data){
            edges[e[0]][e[1]] = ED(new_data);
        }

        // Building Graph functions
        void insertVertex(int v_id, const VD& v_data = 0){
            VD v_data_copy(v_data); // copy constractor call of VD class
            vertices[v_id] = v_data_copy;
        }
        void insertVertices(const int* v_ids, int s, const VD* v_datas = nullptr){
            for(int i = 0; i < s; ++i){
                if(v_datas == nullptr)
                    insertVertex(*(v_ids+i));
                else
                    insertVertex(*(v_ids+i), *(v_datas+i));
            }
        }
        void insertEdge(const vector<int>& e, const ED& e_data = 0){    // Each vector is a pair (v_from_id, v_to_id)
            ED e_data_copy(e_data); // copy constractor call of ED class
            edges[e[0]][e[1]] = e_data_copy;
            if (directed == false)
                edges[e[1]][e[0]] = e_data_copy;
        }
        void insertEdge(int v_from_id, int v_to_id, const ED& e_data = 0){    // Each vector is a pair (v_from_id, v_to_id)
            ED e_data_copy(e_data); // copy constractor call of ED class
            edges[v_from_id][v_to_id] = e_data_copy;
            if (directed == false)
                edges[v_to_id][v_from_id] = e_data_copy;
        }


        void insertEdges(const vector<int>* es, int s, const ED* e_datas = nullptr){
            for(int i = 0; i < s; ++i){
                if (e_datas == nullptr)
                    insertEdge(*(es+i));
                else
                    insertEdge(*(es+i), *(e_datas+i));
            }
        }
        void concatenateGraph(const Graph<VD, ED>& G){
            // Insert all G vertices and edges to this graph
            Graph<VD, ED>& cast_G = const_cast<Graph<VD, ED>&>(G);
            for(auto& v : cast_G.verticesIterator()){
                insertVertex(v->first, v->second);
                for(auto& e : cast_G.edgesIterator(v->first)){
                    insertEdge(v->first, e->first, e->second);
                }
            }
        }
        void removeVertex(int v_id){
            // free all edges of this vertex
            // unefficient implementation
            int i = 0, size = edges[v_id].size();
            // remove edges from v_id
            for (auto& [v_id_to, edges_data] : edges[v_id]){ // bad iterations. delteing from edges during the interator operation cause to an error
                if(i++ == size) break;
                removeEdge(v_id, v_id_to);
            }
            cout<<"next"<<endl;
            for (auto& [v_id_from, edges_list] : edges){    // remove edges to v_id
                if (edges_list[v_id] != 0)
                    removeEdge(v_id_from, v_id);
            
            }
            // free data memory of vertex
            if (vertices[v_id] != 0){
                delete &(vertices[v_id]);
            }
            vertices.erase(v_id);
        }
        void removeVertices(const int* v_ids, int s){
            for(int i = 0; i < s; ++i){
               removeVertex(*(v_ids+i));
            }
        }
        void removeEdge(const vector<int>& e){    // Each vector is a pair (v_from_id, v_to_id)
            // free data memory of edge
            if (edges[e[0]][e[1]] != 0){
                delete &(edges[e[0]][e[1]]);
            }
            edges[e[0]].erase(e[1]);
            if (edges[e[0]].size() == 0){
                edges.erase(e[0]);
            }
        }
        void removeEdge(const int& v_id_from, const int& v_id_to){    // edge = (v_from_id, v_to_id)
            // free data memory of edge
                    if (edges[v_id_from][v_id_to] != 0){
                delete &(edges[v_id_from][v_id_to]);
            }
            cout << "! remove (" << v_id_from <<","<<v_id_to<<")" << endl;
            edges[v_id_from].erase(v_id_to);
            if (edges[v_id_from].size() == 0){
                cout << "! remove adgj list of " << v_id_from  << endl;
                edges.erase(v_id_from);
            }
            cout << *this;
        }    
        void removeEdges(const vector<int>* e, int s){
            for(int i = 0; i < s; ++i)
                removeEdge(*(e+i));
        }
        void removeAllEdges(){
            for(auto& [v_id_from, edges_list] : edges)
                for(auto& [v_id_to, e_data] : edges_list)
                    removeEdge(vector<int>(v_id_from, v_id_to));
        }
        void removeGraph(){
            removeAllEdges();
            for(auto& [v_id, v_data] : vertices)
                removeVertex(v_id);
        }

        // Get properties functions
        unsigned int verticesSize() { return vertices.size(); }
        unsigned int edgesSize(){
            int edges_num = 0;
            for(auto& [key, ver_lst] : edges){
                edges_num += ver_lst.size();
            }
            if (!directed)  // unconsidering duplicate edges in undirected graph
                edges_num /= 2;
            return edges_num;
        }
        vector<unsigned int>& size() {      // returns a 2-unsigned-int-items vector (n,m)
                                            //  where n=#vertices and m=#edges
            return *(new vector<unsigned int>({verticesSize(), edgesSize()}));  
        }
        bool empty() { return verticesSize() == 0; }

        // Other utility functions
        Graph& undirectGraph();     // Turn the graph to be undirected
        Graph& reverseGraph();      // Reverse all the edges in graph
        void print(std::ostream& out){
            out << "V={";
            for(auto& [v_id,v_data] : vertices){
                out << v_id << ", ";
            }
            out << "}" << endl << "E={";
            for(auto& [v_from, list_e] : edges)
                for(auto& [v_to, e_data] : edges[v_from])
                    out << "(" << v_from << ", " << v_to << "), ";
            out << "}" << endl;
        }
        friend std::ostream& operator<<(std::ostream& out, const Graph& g){
            const_cast<Graph&>(g).print(out);
            return out;
        };
};



