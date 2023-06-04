#include "Graph.hpp"
#include "Constraint.hpp"
#include <bits/stdc++.h>    // for unordered_set, priority_queue, sets

template<class T>
class Vertex{
    typedef enum v_type {CONSTRAINT, VARIABLE} v_type;
    protected:
        v_type ver_type;

};

template<class T>
class ConstraintVertex : Vertex{
    private:
        Constraint<T> con;
        set<VareiableVertex> con_vertices; // all the VareiableVertex that goes to the constraint
        bool commutativ;
    public:
        ConstraintVertex(v_type t, Constraint<T> c, bool commut = true) 
        : ver_type(t), con(c), commutativ(commut) {}
        
        // iterator of con_vertices??
};

template<class T>
class VareiableVertex : Vertex{
    using set<T> = Domain<T>; // what is the container of the domain???
    private:
        int v_id; 
        Domain<T> D;
    public:
        VareiableVertex(v_type t, int id, Domain<T> d) 
        : ver_type(t), v_id(id), D(d) {}
        VareiableVertex(v_type t) 
        : ver_type(t) {}
        void InsertValue(T& val){ D.insert(val); }
        bool empty(){ return D.empty(); }
    
    // iterator for the domain variable??
};

// class Edge{
//     public:
//         int from;
//         int to;
// };