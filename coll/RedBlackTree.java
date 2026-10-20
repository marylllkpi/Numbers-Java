import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/** Hard task: red-black tree of integers (insert, delete, traversals, display). */
public class RedBlackTree {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        int key;
        boolean color;
        Node left;
        Node right;
        Node parent;

        Node(int key, boolean color) {
            this.key = key;
            this.color = color;
        }

        @Override
        public String toString() {
            return key + (color == RED ? "(R)" : "(B)");
        }
    }

    private final Node nil = new Node(0, BLACK); // one common black leaf
    private Node root;
    private int size = 0;

    public RedBlackTree() {
        nil.left = nil;
        nil.right = nil;
        nil.parent = nil;
        root = nil;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return root == nil;
    }

    public void clear() {
        root = nil;
        size = 0;
    }

    public boolean contains(int key) {
        return find(key) != nil;
    }

    private Node find(int key) {
        Node x = root;
        while (x != nil && x.key != key) {
            x = key < x.key ? x.left : x.right;
        }
        return x;
    }

    // ---------- Rotations ----------

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != nil) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == nil) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != nil) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == nil) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    // ---------- Insertion ----------

    /** Adds a number. Returns false if the number is already in the tree. */
    public boolean insert(int key) {
        Node parent = nil;
        Node x = root;
        while (x != nil) {
            parent = x;
            if (key == x.key) {
                return false;
            }
            x = key < x.key ? x.left : x.right;
        }
        Node z = new Node(key, RED); // a new node is always red
        z.left = nil;
        z.right = nil;
        z.parent = parent;
        if (parent == nil) {
            root = z;
        } else if (key < parent.key) {
            parent.left = z;
        } else {
            parent.right = z;
        }
        size++;
        fixAfterInsert(z);
        return true;
    }

    private void fixAfterInsert(Node z) {
        while (z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                Node uncle = z.parent.parent.right;
                if (uncle.color == RED) {                 // case 1: red uncle -> recolor
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {            // case 2: turn into case 3
                        z = z.parent;
                        rotateLeft(z);
                    }
                    z.parent.color = BLACK;               // case 3: recolor and rotate
                    z.parent.parent.color = RED;
                    rotateRight(z.parent.parent);
                }
            } else {
                Node uncle = z.parent.parent.left;
                if (uncle.color == RED) {
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    // ---------- Deletion ----------

    /** Removes a number. Returns false if the number is not in the tree. */
    public boolean delete(int key) {
        Node z = find(key);
        if (z == nil) {
            return false;
        }
        Node y = z;
        boolean removedColor = y.color;
        Node x;
        if (z.left == nil) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == nil) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);                         // successor
            removedColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        size--;
        if (removedColor == BLACK) {
            fixAfterDelete(x);
        }
        return true;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == nil) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    private Node minimum(Node x) {
        while (x.left != nil) {
            x = x.left;
        }
        return x;
    }

    private void fixAfterDelete(Node x) {
        while (x != root && x.color == BLACK) {
            if (x == x.parent.left) {
                Node w = x.parent.right;
                if (w.color == RED) {                     // case 1: red sibling
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if (w.left.color == BLACK && w.right.color == BLACK) { // case 2
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.right.color == BLACK) {         // case 3
                        w.left.color = BLACK;
                        w.color = RED;
                        rotateRight(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;             // case 4
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                Node w = x.parent.left;
                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if (w.right.color == BLACK && w.left.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.left.color == BLACK) {
                        w.right.color = BLACK;
                        w.color = RED;
                        rotateLeft(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    // ---------- Traversals ----------

    /** In-order traversal (ascending order). */
    public List<String> inOrder() {
        List<String> result = new ArrayList<String>();
        inOrder(root, result);
        return result;
    }

    private void inOrder(Node n, List<String> out) {
        if (n == nil) {
            return;
        }
        inOrder(n.left, out);
        out.add(n.toString());
        inOrder(n.right, out);
    }

    /** Pre-order traversal (node, left, right). */
    public List<String> preOrder() {
        List<String> result = new ArrayList<String>();
        preOrder(root, result);
        return result;
    }

    private void preOrder(Node n, List<String> out) {
        if (n == nil) {
            return;
        }
        out.add(n.toString());
        preOrder(n.left, out);
        preOrder(n.right, out);
    }

    /** Post-order traversal (left, right, node). */
    public List<String> postOrder() {
        List<String> result = new ArrayList<String>();
        postOrder(root, result);
        return result;
    }

    private void postOrder(Node n, List<String> out) {
        if (n == nil) {
            return;
        }
        postOrder(n.left, out);
        postOrder(n.right, out);
        out.add(n.toString());
    }

    /** Level-order traversal (level by level). */
    public List<String> levelOrder() {
        List<String> result = new ArrayList<String>();
        if (root == nil) {
            return result;
        }
        Queue<Node> queue = new ArrayDeque<Node>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node n = queue.remove();
            result.add(n.toString());
            if (n.left != nil) {
                queue.add(n.left);
            }
            if (n.right != nil) {
                queue.add(n.right);
            }
        }
        return result;
    }

    // ---------- Display ----------

    /** Draws the tree turned 90 degrees: the right subtree is on top, the root is on the left. */
    public String draw() {
        if (root == nil) {
            return "(the tree is empty)";
        }
        StringBuilder sb = new StringBuilder();
        draw(root, 0, sb);
        return sb.toString();
    }

    private void draw(Node n, int depth, StringBuilder sb) {
        if (n == nil) {
            return;
        }
        draw(n.right, depth + 1, sb);
        for (int i = 0; i < depth; i++) {
            sb.append("    ");
        }
        sb.append(n).append('\n');
        draw(n.left, depth + 1, sb);
    }

    public int height() {
        return height(root);
    }

    private int height(Node n) {
        if (n == nil) {
            return 0;
        }
        return 1 + Math.max(height(n.left), height(n.right));
    }

    // ---------- Check of red-black properties ----------

    /** Returns null if the tree is a correct red-black tree, otherwise the description of the problem. */
    public String validate() {
        if (root == nil) {
            return null;
        }
        if (root.color != BLACK) {
            return "The root is not black";
        }
        try {
            blackHeight(root, Long.MIN_VALUE, Long.MAX_VALUE);
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
        return null;
    }

    private int blackHeight(Node n, long min, long max) {
        if (n == nil) {
            return 1;
        }
        if (n.key <= min || n.key >= max) {
            throw new IllegalStateException("Binary search tree order is broken at " + n.key);
        }
        if (n.color == RED && (n.left.color == RED || n.right.color == RED)) {
            throw new IllegalStateException("Red node " + n.key + " has a red child");
        }
        int left = blackHeight(n.left, min, n.key);
        int right = blackHeight(n.right, n.key, max);
        if (left != right) {
            throw new IllegalStateException("Different number of black nodes on paths at " + n.key);
        }
        return left + (n.color == BLACK ? 1 : 0);
    }

    /** Number of black nodes on any path from the root to a leaf (the tree must be valid). */
    public int blackHeight() {
        return root == nil ? 0 : blackHeight(root, Long.MIN_VALUE, Long.MAX_VALUE) - 1;
    }
}
