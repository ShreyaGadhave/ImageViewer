
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {

    static DoublyLinkedList gallery = new DoublyLinkedList();

    static JFrame frame;
    static JLabel imageLabel;
    static JLabel statusLabel;

    static final String DATABASE = "imageflow_gallery.txt";

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            if (GraphicsEnvironment.isHeadless()) {
                System.out.println("GUI is not supported!");
                return;
            }

            loadImages();
            createGUI();
            showCurrentImage();

        });
    }

    // Create GUI
    static void createGUI() {

        frame = new JFrame("IMAGEFLOW - Image Viewer");

        frame.setSize(1000, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel heading = new JLabel(
            "IMAGEFLOW - IMAGE VIEWER",
            SwingConstants.CENTER
        );

        heading.setFont(new Font("Arial", Font.BOLD, 26));

        frame.add(heading, BorderLayout.NORTH);

        imageLabel = new JLabel(
            "No images uploaded!",
            SwingConstants.CENTER
        );

        imageLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        imageLabel.setBackground(Color.LIGHT_GRAY);
        imageLabel.setOpaque(true);

        frame.add(imageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add Image");
        JButton previousButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");
        JButton firstButton = new JButton("First");
        JButton lastButton = new JButton("Last");
        JButton deleteButton = new JButton("Delete");
        JButton displayButton = new JButton("Display All");

        buttonPanel.add(addButton);
        buttonPanel.add(firstButton);
        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(lastButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(displayButton);

        statusLabel = new JLabel("Total Images: 0");

        JPanel bottomPanel = new JPanel(new BorderLayout());

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(e -> addImage());

        nextButton.addActionListener(e -> {
            if (!gallery.nextImage()) {
                JOptionPane.showMessageDialog(
                    frame, "No next image available!"
                );
            }
            showCurrentImage();
        });

        previousButton.addActionListener(e -> {
            if (!gallery.previousImage()) {
                JOptionPane.showMessageDialog(
                    frame, "No previous image available!"
                );
            }
            showCurrentImage();
        });

        firstButton.addActionListener(e -> {
            gallery.firstImage();
            showCurrentImage();
        });

        lastButton.addActionListener(e -> {
            gallery.lastImage();
            showCurrentImage();
        });

        deleteButton.addActionListener(e -> deleteImage());

        displayButton.addActionListener(e -> displayAllImages());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Add image with exception handling
    static void addImage() {

        try {

            JFileChooser chooser = new JFileChooser();

            FileNameExtensionFilter filter =
                new FileNameExtensionFilter(
                    "Image Files (JPG, JPEG, PNG)",
                    "jpg", "jpeg", "png"
                );

            chooser.setFileFilter(filter);

            int result = chooser.showOpenDialog(frame);

            if (result == JFileChooser.APPROVE_OPTION) {

                File selectedFile = chooser.getSelectedFile();

                // FileNotFoundException
                if (!selectedFile.exists()) {
                    throw new FileNotFoundException(
                        "Selected image does not exist!"
                    );
                }

                // Read image
                BufferedImage image = ImageIO.read(selectedFile);

                if (image == null) {
                    JOptionPane.showMessageDialog(
                        frame, "Invalid image format!"
                    );
                    return;
                }

                gallery.addImage(selectedFile.getAbsolutePath());

                saveImages();
                showCurrentImage();

            }

        }
        catch (FileNotFoundException e) {

            JOptionPane.showMessageDialog(
                frame,
                "File Not Found: " + e.getMessage()
            );

        }
        catch (IOException e) {

            JOptionPane.showMessageDialog(
                frame,
                "IO Exception: Unable to read image!"
            );

        }
        catch (NullPointerException e) {

            JOptionPane.showMessageDialog(
                frame,
                "Null Pointer Exception: Image path is missing!"
            );
        }
    }

    // Display current image
    static void showCurrentImage() {

        try {

            String path = gallery.getCurrentImage();

            // NullPointerException prevention
            if (path == null) {
                imageLabel.setIcon(null);
                imageLabel.setText("No images available!");
                updateStatus();
                return;
            }

            File file = new File(path);

            // FileNotFoundException
            if (!file.exists()) {
                throw new FileNotFoundException(
                    "Image file not found!"
                );
            }

            BufferedImage image = ImageIO.read(file);

            if (image == null) {
                imageLabel.setIcon(null);
                imageLabel.setText("Invalid image format!");
                return;
            }

            Image scaledImage = image.getScaledInstance(
                800, 500, Image.SCALE_SMOOTH
            );

            imageLabel.setIcon(new ImageIcon(scaledImage));
            imageLabel.setText("");

            updateStatus();

        }
        catch (FileNotFoundException e) {

            JOptionPane.showMessageDialog(
                frame,
                "File Not Found: " + e.getMessage()
            );

        }
        catch (IOException e) {

            JOptionPane.showMessageDialog(
                frame,
                "IO Exception: Unable to display image!"
            );

        }
        catch (NullPointerException e) {

            JOptionPane.showMessageDialog(
                frame,
                "Null Pointer Exception: No image selected!"
            );

        }
    }

    // Delete current image
    static void deleteImage() {

        if (gallery.totalImages() == 0) {

            JOptionPane.showMessageDialog(
                frame, "No images to delete!"
            );

            return;
        }

        int choice = JOptionPane.showConfirmDialog(
            frame,
            "Are you sure you want to delete this image?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            gallery.deleteImage();

            saveImages();
            showCurrentImage();
        }
    }

    // Display all images
    static void displayAllImages() {

        try {

            String images = gallery.displayAll();

            if (images == null || images.isEmpty()) {
                throw new NullPointerException(
                    "Gallery is empty!"
                );
            }

            JTextArea area = new JTextArea(images);

            area.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(area);

            scrollPane.setPreferredSize(
                new Dimension(600, 300)
            );

            JOptionPane.showMessageDialog(
                frame,
                scrollPane,
                "All Images",
                JOptionPane.INFORMATION_MESSAGE
            );

        }
        catch (NullPointerException e) {

            JOptionPane.showMessageDialog(
                frame,
                "Null Pointer Exception: No images available!"
            );
        }
    }

    // Update status
    static void updateStatus() {

        statusLabel.setText(
            "Total Images: " + gallery.totalImages()
            + " | Current Image: "
            + gallery.getCurrentPosition()
        );
    }

    // Save image paths
    static void saveImages() {

        try (
            BufferedWriter writer =
                new BufferedWriter(new FileWriter(DATABASE))
        ) {

            ImageNode temp = gallery.head;

            while (temp != null) {

                writer.write(temp.imagePath);
                writer.newLine();

                temp = temp.next;
            }

        }
        catch (FileNotFoundException e) {

            JOptionPane.showMessageDialog(
                frame,
                "File Not Found: Cannot save gallery!"
            );

        }
        catch (IOException e) {

            JOptionPane.showMessageDialog(
                frame,
                "IO Exception: Error saving gallery!"
            );

        }
    }

    // Load saved images
    static void loadImages() {

        File file = new File(DATABASE);

        if (!file.exists()) {
            return;
        }

        try (
            BufferedReader reader =
                new BufferedReader(new FileReader(file))
        ) {

            String path;

            while ((path = reader.readLine()) != null) {

                try {

                    File imageFile = new File(path);

                    if (!imageFile.exists()) {
                        throw new FileNotFoundException(
                            "Saved image not found: " + path
                        );
                    }

                    BufferedImage image = ImageIO.read(imageFile);

                    if (image != null) {
                        gallery.addImage(path);
                    }

                }
                catch (FileNotFoundException e) {

                    System.out.println(e.getMessage());

                }
                catch (IOException e) {

                    System.out.println(
                        "Unable to read image: " + path
                    );

                }
                catch (NullPointerException e) {

                    System.out.println(
                        "Invalid image path encountered!"
                    );
                }
            }

        }
        catch (FileNotFoundException e) {

            System.out.println("Gallery file not found!");

        }
        catch (IOException e) {

            System.out.println(
                "Error loading gallery: " + e.getMessage()
            );
        }
    }
}
