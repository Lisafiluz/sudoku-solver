#include <functional>
#include <iostream>
using namespace std;

template <class T = int>
class Constraint{
    private:
        unsigned int num_of_args;
        function<bool<const T*...>> c;
    public:
        // func is a pointer to function that returns bool and its parameters are num_of_args arguments
        Constraint(int n, function<bool<const T*...>> func) : num_of_args(n), func(c) {}
        bool check_constraint(const T* args...){
            return c(args);
        }
};

// template <class T = int>
// class BineryConstraint : Constraint{
// };

// template <class T = int>
// class SymmetricConstraint : Constraint{

// };