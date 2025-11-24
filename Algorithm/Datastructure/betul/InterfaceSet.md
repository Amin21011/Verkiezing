# Research Report — Set Interface
**Betül Aydin**

Understanding and using the right data structures is an essential skill for software developers. In Java, the `Set` interface is used when a program needs to store unique values. However, many beginner developers still choose lists such as `ArrayList`, even when a `Set` would be more efficient and reliable. This often results in unnecessary duplicates, reduced performance, and issues with data integrity.

This research investigates how the `Set` interface works and in which situations it is the best choice for handling unique data in Java.

**Central Theme:**  
“Data structures in Java and selecting the right collection type for unique values.”

The theoretical framework is based on Java documentation, literature about data structures, and general principles for choosing efficient and appropriate collections.

---

## Problem Statement
Beginning Java developers often struggle to choose the correct data structure for storing unique values. Many default to using Lists, which allow duplicates and require extra logic to remove them. This not only slows down programs but also complicates the code and increases the risk of errors. This problem is especially common in educational settings and small software projects.

---

## Objective
The goal of this research is to explain the `Set` interface, the differences between its implementations, how it prevents duplicates, and how two specific `Set` types—`HashSet` and `TreeSet`—compare in terms of performance and behavior.

---

## Main Research Question
**“How does the Set interface in Java work, and in which situations is this data structure the most suitable choice for handling unique data?”**

---

## Sub-Questions
1. What is the `Set` interface in Java, and what are its key characteristics?
2. Which types of `Set`s exist in Java, and how do they differ?
3. How does Java prevent duplicates in a `Set`?
4. How do `HashSet` and `TreeSet` compare in terms of performance and behavior? ← Demo

These sub-questions together answer the main question and provide a complete understanding of `Set` usage in Java.

---

## Theoretical Framework

### Core Theme and Key Concepts
The main theme of this research is data structures in Java, specifically the `Set` interface.

### Set Interface
A `Set` represents a collection that cannot contain duplicate elements. It is ideal for storing unique data such as usernames, IDs, or filtered inputs.

### Types of Sets

**HashSet**
- Uses hashing internally
- Does not preserve order
- Fastest `Set` type

**LinkedHashSet**
- Similar performance as `HashSet`
- Preserves insertion order
- Slightly more memory usage

**TreeSet**
- Stores elements in a sorted order (alphabetically or numerically)
- Slower than `HashSet` due to sorting
- Useful when sorted results are required

### ICT Norms

**Data Quality & Integrity**
- `Set`s prevent duplicates automatically, increasing the integrity of stored data.

**Efficiency & Performance**
- `Set`s, especially `HashSet`, provide fast operations which support efficient software design.

**Reliability**
- Predictable behavior and clear rules help developers produce maintainable, bug-free programs.

**Sustainable Processing**
- Efficient data structures minimize unnecessary data processing and reduce resource usage.

---

## What is the Set interface in Java, and what are its key characteristics?
A `Set` in Java is a data structure that stores unique values:
- It never allows duplicates
- Every element appears only once
- The order is not guaranteed unless you use a specific `Set` type

**Examples:**
- Storing unique usernames
- Keeping track of visited pages
- Filtering user input so duplicates are removed automatically

Java checks every element before adding it. If the element already exists, it is simply ignored. This makes `Set`s very reliable for data integrity.

---

## Which types of Sets exist in Java, and how do they differ?

**HashSet**
- Fastest `Set` type
- Uses hashing internally
- Does not preserve order
- Best choice when you only care about speed and uniqueness

**LinkedHashSet**
- Similar performance as `HashSet`
- Preserves insertion order
- Slightly more memory usage
- Good when you need both speed and predictable order

**TreeSet**
- Stores elements in sorted order (alphabetical or numerical)
- Slower than `HashSet` because sorting takes time
- Very useful when your program needs automatic sorting

**Key Differences:** speed, memory usage, and whether the elements should be ordered or sorted.

---

## How does Java prevent duplicates in a Set?

**HashSet / LinkedHashSet**
1. Calculates a `hashCode()`
2. Checks the “bucket” where this element belongs
3. If another element with the same hash exists, Java compares them using `equals()`
4. If `equals()` returns `true` → the element is considered a duplicate → it is not added

**TreeSet**
- Does not use `hashCode()`
- Uses `compareTo()` (if elements implement `Comparable`) or a `Comparator`
- If `compareTo()` returns 0 → element is considered equal → not added

This ensures that `Set`s always keep unique elements, regardless of the implementation.

---

## How do HashSet and TreeSet compare in terms of performance and behavior?

**Performance**
- **HashSet:** Very fast, best for large datasets
- **TreeSet:** Slower because it keeps elements sorted, good when sorted output is required

**Behavior**
- **HashSet:** No order, optimized for speed
- **TreeSet:** Sorted, but slower
- Both prevent duplicates and are reliable
- Used for different purposes depending on the need

**Demo Example:**  
Adding 100,000 elements:
- `HashSet`: ~17 ms
- `TreeSet`: ~64 ms

This shows that `HashSet` is much faster, while `TreeSet` provides automatic sorting at the cost of time.

---

## Results
- The `Set` interface is the best data structure in Java when unique values are required.
- `Set`s automatically prevent duplicates, improving data quality, integrity, and performance.
- Implementation advantages:
    - **HashSet:** maximum speed
    - **LinkedHashSet:** predictable ordering
    - **TreeSet:** sorted output
- Performance tests confirm that `HashSet` is faster, while `TreeSet` adds automatic sorting.
- `Set`s help developers reduce bugs, avoid unnecessary duplicate handling, and create efficient, reliable programs. Therefore, `Set`s are the most suitable choice when uniqueness is required.

---

## References
- Oracle. (2025, October 20). *Set (Java Platform SE 8)*. Oracle. [Link](https://docs.oracle.com/javase/8/docs/api/java/util/Set.html)
- Oracle. (2025, October 20). *HashSet (Java Platform SE 8)*. Oracle. [Link](https://docs.oracle.com/javase/8/docs/api/java/util/HashSet.html)
- Oracle. (2025, October 20). *TreeSet (Java Platform SE 8)*. Oracle. [Link](https://docs.oracle.com/javase/8/docs/api/java/util/TreeSet.html)
- Sierra, K., Bates, B., & Gee, T. (2022). *Head First Java (3rd ed.)*. O’Reilly Media, Inc.
- Kumar, A. (2025, August 14). *HashSet vs TreeSet vs LinkedHashSet – Comparison Table and Best Use Cases*. prgrmmng.com. [Link](https://prgrmmng.com/hashset-vs-treeset-vs-linkedhashset)  
