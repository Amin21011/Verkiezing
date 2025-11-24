# Interface List with implementation classes ArrayList, LinkedList

## The central question of this report is:
In what ways do the implementations of the Java List interface, such as ArrayList and LinkedList, differ in their internal structure and performance for different types of operations?

### What is an ArrayList and an LinkedList? 
An ArrayList is a list where all items are stored in a row, so you can quickly get any item if you know its position. Adding or removing items in the middle can be slow because other items need to move. A LinkedList stores items like a chain, where each item knows the one before and after it. This makes adding or removing items in the middle easy once you are already at the right position, but finding a specific item takes longer because you must go through the list step by step.

### Sub questions:
- How are the ArrayList and LinkedList classes internally structured, and which algorithms do they use for adding, removing, and retrieving elements?
- In which situations does an ArrayList perform better than a LinkedList, and vice versa?
- How does the choice between ArrayList and LinkedList affect memory usage and overall efficiency in real-world applications?
- How do the different implementations of the List interface impact code maintainability and ease of use for developers?

### How are the ArrayList and LinkedList classes internally structured, and which algorithms do they use for adding, removing, and retrieving elements?
An ArrayList basically works like a normal array that can grow whenever it gets full. All the elements sit next to each other in memory, which makes accessing something by its index really fast (Oracle, 2023a). Adding something at the end is usually quick, but if the array is full, it has to create a bigger one and copy everything over. Adding or removing things in the middle is slower because elements need to shift to keep the order correct (Oracle, 2024d).

A LinkedList works differently. It’s made up of separate nodes, and each node points to the next and the previous one (Oracle, 2023b). Since the elements aren’t stored next to each other, you can’t jump straight to an index — you have to walk through the list from either the start or the end, which makes accessing by index slower. Adding or removing something at the beginning or end is very fast because only a couple of pointers need to be updated. Operations in the middle are possible too, but you still need to walk to the right spot first, so that also takes linear time (Oracle, 2024d).


### In which situations does an ArrayList perform better than a LinkedList, and vice versa?
An ArrayList is great when you need to quickly access an element by its index. It stores all its elements in a single, continuous block of memory, which makes retrieving items very fast. Adding elements at the end is usually quick too, unless the internal array has to resize to make room for more items. This makes ArrayList ideal for situations where you mostly read data or append new elements at the end (Oracle, 2023a).

A LinkedList, on the other hand, is better when you frequently add or remove items, especially in the middle or at the start of the list. It’s made up of separate nodes that are linked together, so instead of shifting large chunks of data, you just change the links between nodes. This structure makes insertions and deletions much faster than in an ArrayList for these cases (Oracle, 2023b).

When looking at performance, an ArrayList provides O(1) time for random access because you can go straight to any index. But inserting or removing elements in the middle or start takes O(n), since the elements have to be shifted. A LinkedList doesn’t allow fast random access—it takes O(n) to reach a specific element—but once you’re at the correct node, adding or removing items is very quick (O(1)). One downside is that LinkedLists use more memory, as each node has extra references to the previous and next nodes (GeeksforGeeks, 2025).

So, in short: choose an ArrayList if your main focus is fast access and appending elements at the end. Go for a LinkedList if your application often involves inserting or removing elements in different positions throughout the list. Both have their strengths depending on how you plan to use them.


### How does the choice between ArrayList and LinkedList affect memory usage and overall efficiency in real-world applications?
An ArrayList stores its elements in a single, continuous block of memory, and it’s implemented as a resizable array (Oracle, 2023a). This setup keeps memory usage relatively low, because each element only needs space for its value. The continuous structure also makes accessing elements really fast, since the index points directly to the memory location. The downside is that when the array gets full, a bigger array has to be created and all the elements copied over. This resizing temporarily uses more memory and slows things down (Oracle, 2023a).

A LinkedList, on the other hand, stores each element in a separate node that has links to both the previous and next elements, making it a doubly linked list (Oracle, 2023b). Because of these extra links, a LinkedList uses more memory per element than an ArrayList. The advantage is that adding or removing elements is easy—just update a few links—without moving all the other elements. The downside is that accessing a specific element is slower, because you have to go through the list one node at a time (Oracle, 2023b).

In practice, ArrayLists are usually better if your program reads data a lot and memory efficiency matters. LinkedLists are better if your program needs to insert or remove elements frequently, even though they use more memory.


### How do the different implementations of the List interface impact code maintainability and ease of use for developers?
An ArrayList is usually the better choice when you need to quickly access elements by their index. Because it’s based on an array, you can get any element really efficiently, which makes it perfect for situations where you read data more often than you change it. Adding items at the end is usually fast, although if the array has to resize, performance can slow down a bit temporarily (Oracle, 2024d).

A LinkedList, on the other hand, works better when your program often adds or removes elements, especially at the start or in the middle of the list. Since it’s made of nodes linked together in both directions, you can insert or delete elements without shifting large chunks of the list, which can make these operations faster than in an ArrayList (Oracle, 2024d).

So, in short: go for an ArrayList if you want fast indexed access and mostly read data, and pick a LinkedList if you expect to do a lot of adding or removing elements throughout the list.


### Conclusion
In this report, we looked at the main differences between the Java List implementations, ArrayList and LinkedList, focusing on their structure, performance, memory usage, and how easy they are to work with.

An ArrayList is basically a resizable array. It’s great if you need fast access to elements by index and often add items at the end. Its predictable array structure makes it simple to use and maintain, especially if your program mostly reads data. The downside is that adding or removing elements in the middle or start can be slow, because all the following elements have to shift, and resizing the array temporarily uses more memory (Oracle, 2023a).

A LinkedList, on the other hand, is made of nodes that are doubly linked. This makes it better for programs where you frequently add or remove items, especially in the middle or beginning. Each element uses more memory, and it’s slower to access elements by index, but inserting or deleting doesn’t require shifting everything else (Oracle, 2023b). This flexibility can make maintenance easier in modification-heavy programs, though the code might be a bit harder for beginners to understand.

Overall, choosing between ArrayList and LinkedList depends on what your program needs. If you want fast access and lower memory use, go with ArrayList. If you expect a lot of updates and changes, LinkedList is more efficient, even if it uses more memory and the code is slightly more complex. Knowing these differences helps developers pick the right one, which improves both performance and maintainability.


### References
1. Oracle. (2023a). ArrayList (Java Platform SE 20). https://docs.oracle.com/en/java/javase/20/docs/api/java.base/java/util/ArrayList.html
2. Oracle. (2023b). LinkedList (Java Platform SE 20). https://docs.oracle.com/en/java/javase/20/docs/api/java.base/java/util/LinkedList.html
3. Oracle. (2023c). List Interface (Java Platform SE 20). https://docs.oracle.com/en/java/javase/20/docs/api/java.base/java/util/List.html
4. Oracle. (2024d). List implementations. https://docs.oracle.com/javase/tutorial/collections/implementations/list.html
5. Geeksforgeeks. (2025). ArrayList vs LinkedList https://www.geeksforgeeks.org/java/arraylist-vs-linkedlist-java/
