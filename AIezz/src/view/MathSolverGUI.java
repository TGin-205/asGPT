package view;

import controller.MathController;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class MathSolverGUI extends JFrame {
    
    private final MathController controller;
    private JTextField problemField;
    private JTextArea resultArea;
    private JTextArea historyArea;
    
    public MathSolverGUI() {
        controller = new MathController();
        initComponents();
        setupLayout();
    }
    
    private void initComponents() {
        setTitle("Giải Toán với Mistral");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Input panel
        problemField = new JTextField(30);
        
        // Result area
        resultArea = new JTextArea(15, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 12));
        resultArea.setBackground(Color.WHITE);
        
        // History area
        historyArea = new JTextArea(15, 30);
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Arial", Font.PLAIN, 11));
        historyArea.setBackground(new Color(248, 248, 248));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel - Input
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Bài toán:"));
        inputPanel.add(problemField);
        
        JButton solveBtn = new JButton("Giải");
        JButton clearBtn = new JButton("Xóa");
        JButton helpBtn = new JButton("Hướng dẫn");
        
        inputPanel.add(solveBtn);
        inputPanel.add(clearBtn);
        inputPanel.add(helpBtn);
        
        // Center panel - Result and History
        JPanel centerPanel = new JPanel(new BorderLayout());
        
        // Result panel
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.add(new JLabel("Kết quả:"), BorderLayout.NORTH);
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        
        // History panel
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.add(new JLabel("Lịch sử:"), BorderLayout.NORTH);
        historyPanel.add(new JScrollPane(historyArea), BorderLayout.CENTER);
        
        // Bottom buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton historyBtn = new JButton("Xem lịch sử");
        JButton saveBtn = new JButton("Lưu lịch sử");
        JButton loadBtn = new JButton("Tải lịch sử");
        JButton checkBtn = new JButton("Kiểm tra hệ thống");
        
        bottomPanel.add(historyBtn);
        bottomPanel.add(saveBtn);
        bottomPanel.add(loadBtn);
        bottomPanel.add(checkBtn);
        
        // Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
            resultPanel, historyPanel);
        splitPane.setDividerLocation(500);
        
        centerPanel.add(splitPane, BorderLayout.CENTER);
        centerPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(inputPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        
        // Events
        solveBtn.addActionListener(e -> solveProblem());
        clearBtn.addActionListener(e -> clearAll());
        helpBtn.addActionListener(e -> showHelp());
        historyBtn.addActionListener(e -> showHistory());
        saveBtn.addActionListener(e -> saveHistory());
        loadBtn.addActionListener(e -> loadHistory());
        checkBtn.addActionListener(e -> checkSystem());
        
        // Enter key
        problemField.addActionListener(e -> solveProblem());
    }

    
    private void solveProblem() {
        String problem = problemField.getText().trim();
        if (problem.isEmpty()) {
            showMessage("Vui lòng nhập bài toán!");
            return;
        }
        
        resultArea.setText("🤖 Đang giải...");
        
        // Chạy trong thread riêng để không block UI
        SwingUtilities.invokeLater(() -> {
            try {
                String result = controller.getModel().solveMath(problem);
                resultArea.setText(result);
                problemField.setText("");
            } catch (Exception e) {
                resultArea.setText("Lỗi: " + e.getMessage());
            }
        });
    }
    
    private void clearAll() {
        problemField.setText("");
        resultArea.setText("");
    }
    
    private void showHelp() {
        String help = """
            === Hướng dẫn sử dụng ===
            
            Các loại toán có thể giải:
            • Tính toán: 2 + 3 * 4
            • Phương trình: x^2 + 5x + 6 = 0
            • Hình học: Tính diện tích hình tròn bán kính 5
            • Đại số: Rút gọn (x+1)(x+2)
            • Bài toán ứng dụng
            
            Ví dụ:
            'Giải phương trình x^2 - 5x + 6 = 0'
            'Tính chu vi hình vuông cạnh 4'
            '15 + 25 * 2 = ?'
            """;
        JOptionPane.showMessageDialog(this, help, "Hướng dẫn", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showHistory() {
        java.util.List<String> history = controller.getModel().getHistory();
        StringBuilder sb = new StringBuilder();
        
        if (history.isEmpty()) {
            sb.append("Chưa có bài toán nào.");
        } else {
            for (int i = 0; i < history.size(); i++) {
                sb.append((i + 1)).append(". ").append(history.get(i)).append("\n");
            }
        }
        
        historyArea.setText(sb.toString());
    }
    
    private void saveHistory() {
        String filename = JOptionPane.showInputDialog(this, "Nhập tên file:", "math_history.txt");
        if (filename != null && !filename.trim().isEmpty()) {
            try {
                controller.getModel().saveHistory(filename);
                showMessage("✅ Đã lưu lịch sử vào " + filename);
            } catch (IOException e) {
                showError("Lỗi khi lưu file: " + e.getMessage());
            }
        }
    }
    
    private void loadHistory() {
        String filename = JOptionPane.showInputDialog(this, "Nhập tên file:", "math_history.txt");
        if (filename != null && !filename.trim().isEmpty()) {
            try {
                controller.getModel().loadHistory(filename);
                showMessage("✅ Đã tải lịch sử từ " + filename);
                showHistory(); // Refresh history display
            } catch (IOException e) {
                showError("Lỗi khi đọc file: " + e.getMessage());
            }
        }
    }
    
    private void checkSystem() {
        resultArea.setText("🔍 Kiểm tra hệ thống...");
        
        SwingUtilities.invokeLater(() -> {
            if (controller.getModel().checkOllama()) {
                resultArea.setText("✅ Ollama đã sẵn sàng!");
            } else {
                resultArea.setText("""
                    ❌ Không thể kết nối Ollama!
                    
                    Hãy chắc chắn:
                    1. Đã cài Ollama
                    2. Chạy: ollama serve
                    3. Tải model: ollama pull mistral
                    """);
            }
        });
    }
    
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showError(String error) {
        JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            }
            
            new MathSolverGUI().setVisible(true);
        });
    }
}