Dust code repository contains some dust code. The code was implemented in order to understand the algorithms used there.

Dancer search
Dancer search helps to find dancing partner for males and females with different heights.
Binary tree search algorithm is used to solve this problem.
The logic is simple.
1. If male dancer joins the party, then lower than him but the highest in group female is found from the queue.
2. If female dancer joins the party, then higher than him but the lowest in group male is found from the queue.
3. If no suitable partner found, then dancer goes to the queue.

Maze runner
Maze solver tries to find the shortest path with the lowest weights available. 
Node can move four directions: South, North, West, East.
Walls are marked as "#", starting point is "B" and finish is "T".
Breadth First Search algorithm is used.

Travelling salesman problem
Salesman tries to find the shortest path visiting city only one time. 
Traveller must return to the starting city.
The problem is solved using 2 different approaches.
First algorithm is Kruskal algorithm using minimal spanning tree, which is implemented using disjoint subset data structure.
Second algorithm is Christofides, which used in comparison with the first.
Both of them are approximate search algorithms



