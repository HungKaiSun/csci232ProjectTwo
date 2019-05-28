package huffmantree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 *
 * @author Hung-Kai Sun
 */
public class HuffmanTree {

    public Node root;

    public Map<Character, String> codeTable = new HashMap<>();

    public static class Node implements Comparable<Node> {

        char c;
        int f;
        Node left;
        Node right;

        Node(char c, int f, Node left, Node right) {
            this.c = c;
            this.f = f;
            this.left = left;
            this.right = right;
        }

        Node(char c, int f) {
            this.c = c;
            this.f = f;
            this.left = null;
            this.right = null;
        }

        public int compareTo(Node node) {
            return (this.f - node.f);
        }
    }

    //build Huffman Tree base on the queue
    public HuffmanTree(Queue<Node> queue) {
        while (queue.size() > 1) {
            Node left = queue.poll();
            Node right = queue.poll();
            Node node = new Node('-', left.f + right.f, left, right);
            queue.add(node);
        }
        this.root = queue.peek();

        //initial two maps
        this.initialTable(this.root, "");

    }

    //initial table
    public void initialTable(Node node, String s) {
        if (node.left == null && node.right == null) {
            this.codeTable.put(node.c, s);
            return;
        }
        initialTable(node.left, s + "0");
        initialTable(node.right, s + "1");
    }

    //print code table
    public void printCodeTable() {
        for (Character key : this.codeTable.keySet()) {
            System.out.println(key + "     " + this.codeTable.get(key));
        }
    }

    //encode
    public String encoder(String message) {
        char[] charArray = message.toCharArray();
        String result = "";
        for (int i = 0; i < charArray.length; i++) {
            result += this.codeTable.get(charArray[i]);
        }
        return result;
    }

    //decode
    public String decoder(String message) {
        int index = 0;
        char[] charArray = message.toCharArray();
        String result = "";
        Node temp = this.root;
        while (index < charArray.length) {
            if (temp.left != null && temp.right != null) {
                if (charArray[index] == '0') {
                    temp = temp.left;
                } else {
                    temp = temp.right;
                }
                index++;
            }
            if (temp.left == null && temp.right == null) {
                result = result + temp.c;
                temp = this.root;
            }
        }
        return result;
    }

    public static void main(String[] args) {

        //input txt file
        String inputMessage = "";
        try {
            Scanner scanner = new Scanner(new File("input.txt"));
            inputMessage = scanner.useDelimiter("\\A").next();
            scanner.close();
        } catch (FileNotFoundException e) {
        }

        System.out.println("original message :");
        System.out.println(inputMessage);

        Map<Character, Integer> map = new HashMap<Character, Integer>();
        char[] inputChar = inputMessage.toCharArray();
        for (int i = 0; i < inputChar.length; i++) {
            if (map.containsKey(inputChar[i])) {
                map.put(inputChar[i], map.get(inputChar[i]) + 1);
            } else {
                map.put(inputChar[i], 1);
            }
        }
        System.out.println("");
        System.out.println("frequency table :");
        for (Character key : map.keySet()) {
            System.out.println(key + "      " + map.get(key));
        }
        Queue<Node> queue = new PriorityQueue<>();

        for (Character key : map.keySet()) {
            queue.add(new Node(key, map.get(key)));
        }

        HuffmanTree tree = new HuffmanTree(queue);

        System.out.println();
        System.out.println("code table");
        tree.printCodeTable();

        String encodeMessage = tree.encoder(inputMessage);
        System.out.println();
        System.out.println("encode message");
        System.out.println(encodeMessage);

        String decodeMessage = tree.decoder(encodeMessage);

        //output file
        File outFile = new File("output.txt");
        try {
            PrintWriter output = new PrintWriter(outFile);
            output.print(decodeMessage);
            output.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

}
