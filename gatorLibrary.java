import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class Reservation {
    int patronID;// ID of the patron making the reservation
    int priorityNumber; // Priority assigned to the reservation
    long timeOfReservation; // Time at which the reservation was made
    // Constructor to initialize reservation details
    public Reservation(int patronID, int priorityNumber, long timeOfReservation) {
        this.patronID = patronID;
        this.priorityNumber = priorityNumber;
        this.timeOfReservation = timeOfReservation;
    }
}

class MinHeap {
    private Reservation[] heap;
    private int size;
    private static final int MAX_SIZE = 20;

    public MinHeap() {
        heap = new Reservation[MAX_SIZE];
        size = 0;
    }

    public boolean isEmpty() { // Check if the heap is empty
        return size == 0;
    }
        
     // Insert a new reservation into the heap
    public void insert(int patronID, int priorityNumber, long timeOfReservation) {
        if (size == MAX_SIZE) {
            System.out.println("Heap is full, cannot insert more reservations.");
            return;
        }

        Reservation newReservation = new Reservation(patronID, priorityNumber, timeOfReservation);
        heap[size] = newReservation;
        heapifyUp(size);
        size++;
    }

    // Extract the minimum priority reservation from the heap
    public Reservation extractMin() {
        if (isEmpty()) {
            return null;
        }

        Reservation min = heap[0];
        heap[0] = heap[size - 1];
        size--;
        heapifyDown(0);

        return min;
    }

    // Method to fix the heap structure upwards from a given index
    private void heapifyUp(int index) {
        int parentIndex = (index - 1) / 2;
        while (index > 0 && compare(heap[index], heap[parentIndex]) < 0) {
            swap(index, parentIndex);
            index = parentIndex;
            parentIndex = (index - 1) / 2;
        }
    }

    // Method to fix the heap structure downwards from a given index
    private void heapifyDown(int index) {
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;
        int smallest = index;

        if (leftChild < size && compare(heap[leftChild], heap[smallest]) < 0) {
            smallest = leftChild;
        }

        if (rightChild < size && compare(heap[rightChild], heap[smallest]) < 0) {
            smallest = rightChild;
        }

        if (smallest != index) {
            swap(index, smallest);
            heapifyDown(smallest);
        }
    }

    // Swap two elements in the heap
    private void swap(int i, int j) {
        Reservation temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // Compare two reservations based on priority number and time of reservation
    private int compare(Reservation r1, Reservation r2) {
        if (r1.priorityNumber != r2.priorityNumber) {
            return Integer.compare(r1.priorityNumber, r2.priorityNumber);
        } else {
            return Long.compare(r1.timeOfReservation, r2.timeOfReservation);
        }
    }

     // String representation of the heap
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            result.append(heap[i].patronID);
            if (i < size - 1) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }
}

// Enum for defining colors in a Red-Black Tree
enum Color {
    RED, BLACK
}


// Node class for Red-Black Tree
class Node {
    int key;
    Node parent;
    Node left;
    Node right;
    Color color;

    public Node(int key, Color color) {
        this.key = key;
        this.color = color;
        this.left = null;
        this.right = null;
        this.parent = null;
    }
}

class RedBlackTree {
   
   private Node root;
    private int colorFlipCount;
    

    public RedBlackTree() {
        this.root = null;
        this.colorFlipCount = 0;
    }

     // Insertion of a node with a given key
     public void insert(int key) {
        Node newNode = new Node(key, Color.RED);
        if (root == null) {
            root = newNode;
            root.color = Color.BLACK;
        } else {
            Node parent = null;
            Node current = root;
            while (current != null) {
                parent = current;
                if (key < current.key) {
                    current = current.left;
                } else if (key > current.key) {
                    current = current.right;
                } else {
                    // Key already exists
                    return;
                }
            }
            newNode.parent = parent;
            if (key < parent.key) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
            fixInsert(newNode);
        }
    }


// Fixing the tree structure after insertion
  private void fixInsert(Node node) {
    while (node != null && node != root && node.parent != null && node.parent.color == Color.RED) {
        Node parent = node.parent;
        Node grandParent = parent.parent;

        if (grandParent != null) {
            if (parent == grandParent.left) {
                Node uncle = grandParent.right;

                if (uncle != null && uncle.color == Color.RED) {
                    parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    grandParent.color = Color.RED;
                    node = grandParent;
                } else {
                    if (node == parent.right) {
                        node = parent;
                        rotateLeft(node);
                        parent = node.parent; // Update parent after rotation
                                               
                    }
                    if (parent != null) {
                        parent.color = Color.BLACK;
                    }
                    if (grandParent != null) {
                        grandParent.color = Color.RED;
                        rotateRight(grandParent); 
                    }
                }
            } else {
                Node uncle = grandParent.left;
                if (uncle != null && uncle.color == Color.RED) {
                    parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    grandParent.color = Color.RED;
                    node = grandParent;
                    
                } else {
                    if (node == parent.left) {
                        node = parent;
                        rotateRight(node);
                        parent = node.parent; // Update parent after rotation                        
                    }
                    if (parent != null) {
                        parent.color = Color.BLACK;
                    }
                    if (grandParent != null) {
                        grandParent.color = Color.RED;
                        rotateLeft(grandParent);
                    }
                }
            }
        }
    }
    if (root != null) {
        root.color = Color.BLACK;
    }
}
    // Rotate a node to the left
    private void rotateLeft(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }
        rightChild.parent = node.parent;
        if (node.parent == null) {
            root = rightChild;
        } else if (node == node.parent.left) {
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }
        rightChild.left = node;
        node.parent = rightChild;
         if (node.color != rightChild.color) {
        colorFlipCount++;
        }
    }    

     // Rotate a node to the right
    private void rotateRight(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }
        leftChild.parent = node.parent;
        if (node.parent == null) {
            root = leftChild;
        } else if (node == node.parent.right) {
            node.parent.right = leftChild;
        } else {
            node.parent.left = leftChild;
        }
        leftChild.right = node;
        node.parent = leftChild;
        if (node.color != leftChild.color) {
            colorFlipCount++;
        }
    }
     
    // Deletion of a node with a given key
     public void delete(int key) {
        Node nodeToDelete = search(root, key);
        if (nodeToDelete == null) {
            // Node not found
            return;
        }
        Node deletedNode = deleteNode(nodeToDelete);         
          if (deletedNode != null && deletedNode.color == Color.BLACK) {
            fixDelete(deletedNode); // Increment count for black node deletion
        }
        
    }    
     private Node deleteNode(Node node) {
        Node deletedNode;
        if (node.left == null || node.right == null) {
            deletedNode = node;
        } else {
            deletedNode = successor(node);
        }
        Node child;
        if (deletedNode.left != null) {
            child = deletedNode.left;
        } else {
            child = deletedNode.right;
        }
        if (child != null) {
            child.parent = deletedNode.parent;
        }
        if (deletedNode.parent == null) {
            root = child;
        } else if (deletedNode == deletedNode.parent.left) {
            deletedNode.parent.left = child;
        } else {
            deletedNode.parent.right = child;
        }
        if (deletedNode != node) {
            node.key = deletedNode.key;
        }
        if (deletedNode.color == Color.BLACK) {
            return child;
        }
        return null;
    }

    // Fixing the tree structure after deletion
     private void fixDelete(Node node) {
        while (node!=null && node != root && node.color == Color.BLACK) {
            if (node == node.parent.left) {
                Node sibling = node.parent.right;
                if (sibling.color == Color.RED) {
                    sibling.color = Color.BLACK;
                    node.parent.color = Color.RED;
                    rotateLeft(node.parent);            
                    sibling = node.parent.right;
                }
                 if (sibling.right.color == Color.BLACK)  {
                    sibling.color = Color.RED;
                    node = node.parent;
                } else {
                    if (sibling.right.color == Color.BLACK) {
                        sibling.left.color = Color.BLACK;
                        sibling.color = Color.RED;
                        rotateRight(sibling);            
                        sibling = node.parent.right;
                    }
                    sibling.color = node.parent.color;
                    node.parent.color = Color.BLACK;
                    sibling.right.color = Color.BLACK;                    
                        rotateLeft(node.parent);                       
                        node = root;
                }
            } else {
                Node sibling = node.parent.left;
                if (sibling.color == Color.RED) {
                    sibling.color = Color.BLACK;
                    node.parent.color = Color.RED;
                    rotateRight(node.parent);                     
                    sibling = node.parent.left;
                }
                if (sibling.right.color == Color.BLACK) {
                    sibling.color = Color.RED;
                    node = node.parent;
                } else {
                    if (sibling.left.color == Color.BLACK) {
                        sibling.right.color = Color.BLACK;
                        sibling.color = Color.RED;
                        rotateLeft(sibling);          
                        sibling = node.parent.left;
                    }
                    sibling.color = node.parent.color;
                    node.parent.color = Color.BLACK;
                    sibling.left.color = Color.BLACK;                    
                        rotateRight(node.parent);          
                        node = root;
                    }                    
                }
                node=node.parent;
            }
            if (node != null) {
            node.color = Color.BLACK;
            }
        }
               
    // Helper methods for delete operation
    private Node search(Node node, int key) {
        if (node == null || node.key == key) {
            return node;
        }
        if (key < node.key) {
            return search(node.left, key);
        }
        return search(node.right, key);
    }

    private Node successor(Node node) {
        Node current = node.right;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    public Node findClosestNode(int targetID) {  
      return findClosestNode(root, targetID);
    }
     
    private Node findClosestNode(Node root, int targetID) {
        if (root == null) return null;
        Node closest = root;
        while (root != null) {
            if (Math.abs(root.key - targetID) < Math.abs(closest.key - targetID)) {
                closest = root;
            }
            if (targetID < root.key) {
                root = root.left;
            } else if (targetID > root.key) {
                root = root.right;
            } else {
                return root;
            }
        }
        return closest;
    }     

    public int getColorFlipCount() {
              return colorFlipCount;
    }
  
public Object getRoot() {
    return null;
}
}

class Book {
    int bookID;
    String title;
    String author;
    String availabilityStatus;
    int borrowedBy;
    MinHeap reservationHeap;

    public Book(int bookID, String title, String author, String availabilityStatus, int borrowedBy) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.availabilityStatus = availabilityStatus;
        this.borrowedBy = borrowedBy;
        this.reservationHeap = new MinHeap();
    }
}

class GatorLib {
    private RedBlackTree redBlackTree;
    private Map<Integer, Book> books;
     private static final boolean RED = true;
    private static final boolean BLACK = false;
    private int colorFlipCount;
    
    class Node {
        int key;
        Node left, right;
        boolean color;
        Node(int key) {
            this.key = key;
            this.color = RED;
        }
    }

    public GatorLib() {
        this.redBlackTree = new RedBlackTree();
        this.books = new HashMap<>();
        this.colorFlipCount = 0;
    }
   
    private void insertBook(int bookID, String title, String author, String availabilityStatus, int borrowedBy, BufferedWriter bw) throws IOException {
        if (books.containsKey(bookID)) { // If book already exists
            bw.write("\n"+"Book already exists with ID: " + bookID + "\n");
            return;
        }
        Book book = new Book(bookID, title, author, availabilityStatus, borrowedBy);
        books.put(bookID, book);
        redBlackTree.insert(bookID);
        colorFlipCount += 1; // Update color flip count after insertion
       
    }

    private void borrowBook(int patronID, int bookID, int patronPriority, BufferedWriter bw) throws IOException {
        Book book = books.get(bookID);
        if (book == null) { //if the book is not found in the library
            bw.write("\n" + "Book not found in the Library\n");
            return;
        }
        if ("Yes".equals(book.availabilityStatus)) { // When the book is avaiable
            book.availabilityStatus = "No";
            book.borrowedBy = patronID;
            bw.write("\n" + "Book " + bookID + " Borrowed by Patron " + patronID + "\n");
            
        } else {
            book.reservationHeap.insert(patronID, patronPriority, System.currentTimeMillis());
            bw.write("\n" + "Book " + bookID + " Reserved by Patron " + patronID + "\n");
        }        
     redBlackTree.insert(bookID);
     colorFlipCount += 1; // Update color flip count after borrowing
      }


    private void returnBook(int patronID, int bookID, BufferedWriter bw) throws IOException {   
        Book book = books.get(bookID);
        if (book == null) {
            bw.write("\n" + "Book not found in the Library\n");
            return;
        }
        //Update book status
        book.availabilityStatus = "Yes";
        book.borrowedBy = -1;
        // Check if there are reservations
        if (!book.reservationHeap.isEmpty()) {
            // Get the top patron from the reservation heap
            Reservation reservation = book.reservationHeap.extractMin();
            book.borrowedBy = reservation.patronID;
            bw.write("\n" + "Book " + bookID + " Returned by Patron " + patronID + "\n");
            bw.write("\n" + "Book " + bookID + " Allotted to Patron " + reservation.patronID + "\n");         
        } else {
            bw.write("\n" + "Book " + bookID + " Returned by Patron " + patronID + "\n");
        }
       redBlackTree.delete(bookID);
        colorFlipCount += 1; // Update color flip count after returning*/
    }

private void deleteBook(int bookID, BufferedWriter bw) throws IOException {
    Book book = books.get(bookID);
    if (book == null) {
        bw.write("Book not found in the Library\n");
        return;
    }
    int prevColorFlipCount = redBlackTree.getColorFlipCount(); // Update color flip count before deletion
    books.remove(bookID);
    int currentColorFlipCount = redBlackTree.getColorFlipCount();// Update color flip count after the book removal from the map  
    redBlackTree.delete(bookID);// Delete the book from the Red-Black Tree    
    colorFlipCount += (currentColorFlipCount - prevColorFlipCount);// Calculate the change in color flip count and update the overall count
        if (!book.reservationHeap.isEmpty()) { // Notify patrons about book unavailability
        bw.write("\n"+"Book " + bookID + " is no longer available. Reservations made by Patrons ");
        while (!book.reservationHeap.isEmpty()) {
            Reservation reservation = book.reservationHeap.extractMin();
            bw.write(" "+reservation.patronID + " ");
        }
        bw.write("have been cancelled!\n"+"\n");
    } else {
        bw.write("\n"+"Book "+bookID+" is no longer available.\n");
    }
}

private void printBook(int bookID, BufferedWriter bw) throws IOException {
    Book book = books.get(bookID);
    if (book == null) { // If the book is not found
    bw.write("Book " + bookID + " not found in the Library\n");
    } else {
      String formattedTitle = formatWithSpaces(book.title);
      String formattedAuthor = formatWithSpaces(book.author);
     bw.write("\n"+"BookID = " + book.bookID + "\n" +
            "Title = \"" + formattedTitle + "\"\n" +
            "Author = \"" + formattedAuthor + "\"\n" +
            "Availability = \"" + (book.availabilityStatus.equals("Yes") ? "No" : "Yes") + "\"\n" +
            "BorrowedBy = \"" + (book.borrowedBy == -1 ? "None" : book.borrowedBy) + "\"\n" +
            "Reservations = " + book.reservationHeap + "\n");
    }
}

private String formatWithSpaces(String input) {
    String[] words = input.split("(?=[A-Z])");
    return String.join(" ", words);
}

private void printBooks(int bookID1, int bookID2, BufferedWriter bw) throws IOException {
    for (int i = bookID1; i <= bookID2; i++) {   
        if (books.containsKey(i)) {  // Print the books presenbt in-between
            printBook(i, bw);
        } 
    }
}

public int findClosestBook(int bookID,BufferedWriter bw) throws IOException {
    if (books.containsKey(bookID)) {
        printBook(bookID, bw);; // If the bookID exists, return the same ID
    } else {
        Integer lowerID = null;
        Integer higherID = null;

        for (int id : books.keySet()) {
            if (id < bookID && (lowerID == null || id > lowerID)) {
                lowerID = id;
            }
            if (id > bookID && (higherID == null || id < higherID)) {
                higherID = id;
            }
        }
        // Determine the closest book ID based on proximity
        if (lowerID == null && higherID == null) {
            return -1; // No available book IDs
        } else
         printBook(lowerID, bw);
            printBook(higherID, bw); 
        }           
    return bookID;
}

private void colorFlipCount(BufferedWriter bw) throws IOException {
    int treeColorFlipCount = redBlackTree.getColorFlipCount();
     bw.write("\n"+"Colour Flip Count: " + (colorFlipCount + treeColorFlipCount) + "\n");
    
}

    public void processOperation(String operation) {
       
        System.out.println("Processing Operation: " + operation);
    }

     public void executeOperationsFromFile(String inputFilename) {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilename));
             BufferedWriter bw = new BufferedWriter(new FileWriter(getOutputFilename(inputFilename)))) {

            String line;
            while ((line = br.readLine()) != null) {
                processOperation(line, bw);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String getOutputFilename(String inputFilename) {
        int dotIndex = inputFilename.lastIndexOf('.');
        if (dotIndex == -1) {
            return inputFilename + "_output_file.txt";
        } else {
            return inputFilename.substring(0, dotIndex) + "_output_file.txt";
        }
    }
    public void processOperation(String operation, BufferedWriter bw) throws IOException {
     String[] tokens = operation.split("\\((?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    String command = tokens[0].trim();



    switch (command) {
        case "InsertBook":
            String[] insertArgs = tokens[1].replaceAll("[\"\\)\\s]", "").split(",");
            if (insertArgs.length >= 4) {
                int bookID = Integer.parseInt(insertArgs[0].trim());
                String title = insertArgs[1].trim();
                String author = insertArgs[2].trim();
                String availabilityStatus = insertArgs[3].trim();
                int borrowedBy = -1; // Initial value for a new book
                insertBook(bookID, title, author, availabilityStatus, borrowedBy, bw);
            } else {
                bw.write("Invalid InsertBook operation: " + operation + "\n");
            }
            break;
        case "PrintBook":
            int printBookID = Integer.parseInt(tokens[1].replaceAll("[\\)\\s]", ""));
            printBook(printBookID, bw);
            break;

        case "PrintBooks":
            String[] printBooksArgs = tokens[1].replaceAll("[\\)\\s]", "").split(",");
            int bookID1 = Integer.parseInt(printBooksArgs[0]);
            int bookID2 = Integer.parseInt(printBooksArgs[1]);
            printBooks(bookID1, bookID2, bw);
            break;

        case "BorrowBook":
            String[] borrowArgs = tokens[1].replaceAll("[\\)\\s]", "").split(",");
            int patronID = Integer.parseInt(borrowArgs[0]);
            int borrowBookID = Integer.parseInt(borrowArgs[1]);
            int patronPriority = Integer.parseInt(borrowArgs[2]);
            borrowBook(patronID, borrowBookID, patronPriority, bw);
            break;

        case "ReturnBook":
            String[] returnArgs = tokens[1].replaceAll("[\\)\\s]", "").split(",");
            int returnPatronID = Integer.parseInt(returnArgs[0]);
            int returnBookID = Integer.parseInt(returnArgs[1]);
            returnBook(returnPatronID, returnBookID, bw);
            break;

        case "DeleteBook":
            int deleteBookID = Integer.parseInt(tokens[1].replaceAll("[\\)\\s]", ""));
            deleteBook(deleteBookID, bw);
            break;

        case "FindClosestBook":
            int targetID = Integer.parseInt(tokens[1].replaceAll("[\\)\\s]", ""));
            findClosestBook(targetID, bw);
            break;

        case "ColorFlipCount":
            colorFlipCount(bw);
            break;

        case "Quit":
            bw.write("\n"+"Program Terminated!!\n");
            bw.close();
            System.exit(0);

        default:
            bw.write("Invalid operation: " + operation + "\n");
            break;
    }
}


}

public class gatorLibrary{
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java gatorLib input_file");
            return;
        }

        GatorLib gatorLibrary = new GatorLib();
        gatorLibrary.executeOperationsFromFile(args[0]);
    }
}

