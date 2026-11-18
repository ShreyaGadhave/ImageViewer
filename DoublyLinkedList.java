
class DoublyLinkedList {

    ImageNode head, tail, current;
    int size = 0;

    // Add image
    void addImage(String path) {

        if (path == null || path.trim().isEmpty()) {
            System.out.println("Invalid image path!");
            return;
        }

        ImageNode newNode = new ImageNode(path);

        if (head == null) {
            head = tail = current = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }

        size++;
        System.out.println("Image added successfully!");
    }

    // Get current image
    String getCurrentImage() {

        if (current == null) {
            return null;
        }

        return current.imagePath;
    }

    // Next image
    boolean nextImage() {

        if (current == null) {
            return false;
        }

        if (current.next == null) {
            return false;
        }

        current = current.next;
        return true;
    }

    // Previous image
    boolean previousImage() {

        if (current == null) {
            return false;
        }

        if (current.prev == null) {
            return false;
        }

        current = current.prev;
        return true;
    }

    // First image
    void firstImage() {

        if (head != null) {
            current = head;
        }
    }

    // Last image
    void lastImage() {

        if (tail != null) {
            current = tail;
        }
    }

    // Delete current image
    void deleteImage() {

        if (current == null) {
            System.out.println("No image to delete!");
            return;
        }

        ImageNode temp = current;

        if (temp.prev != null) {
            temp.prev.next = temp.next;
        } else {
            head = temp.next;
        }

        if (temp.next != null) {
            temp.next.prev = temp.prev;
        } else {
            tail = temp.prev;
        }

        current = (temp.next != null)
                ? temp.next : temp.prev;

        size--;

        if (size == 0) {
            head = tail = current = null;
        }

        System.out.println("Image deleted successfully!");
    }

    // Display all images
    String displayAll() {

        StringBuilder result = new StringBuilder();

        ImageNode temp = head;

        while (temp != null) {

            result.append(temp.imagePath).append("\n");
            temp = temp.next;
        }

        return result.toString();
    }

    // Total images
    int totalImages() {
        return size;
    }

    // Current position
    int getCurrentPosition() {

        int position = 1;
        ImageNode temp = head;

        while (temp != null) {

            if (temp == current) {
                return position;
            }

            temp = temp.next;
            position++;
        }

        return 0;
    }
}
