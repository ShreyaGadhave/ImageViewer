
class ImageNode {

    String imagePath;
    ImageNode prev;
    ImageNode next;

    ImageNode(String imagePath) {

        if (imagePath == null) {
            throw new NullPointerException(
                "Image path cannot be null!"
            );
        }

        this.imagePath = imagePath;
        this.prev = null;
        this.next = null;
    }
}
