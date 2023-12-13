import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.awt.Frame;
import java.lang.reflect.Method;
import java.awt.Robot;





public class WeatherApp {

    private JFrame frame;
    private JLabel timeLabel;
    private JLabel temperatureLabel;
    private int mouseX, mouseY;
    private boolean locked = false;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new WeatherApp().initialize();
        });
    }

    private void initialize() {
        frame = new JFrame("Weather App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 260);

        // 设置窗口为无装饰
        frame.setUndecorated(true);

        // 设置背景图片
        setContentPaneBackground(frame, "E://app/calendar/src/晴と雨.jpg");

        timeLabel = new JLabel("Current Time: ");
        temperatureLabel = new JLabel("Temperature: ");


        timeLabel.setOpaque(false);
        timeLabel.setBackground(new Color(0, 0, 0, 0)); // 设置背景透明
        temperatureLabel.setOpaque(false);
        temperatureLabel.setBackground(new Color(0, 0, 0, 0)); // 设置背景透明



        Font labelFont = new Font("微软雅黑", Font.ITALIC, 30);
        timeLabel.setFont(labelFont);
        temperatureLabel.setFont(labelFont);

        timeLabel.setForeground(Color.WHITE);
        temperatureLabel.setForeground(Color.WHITE);



        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        panel.add(timeLabel);
        panel.add(temperatureLabel);
        panel.setOpaque(false);

        frame.add(panel, BorderLayout.CENTER);

        // 添加鼠标监听器，实现窗口拖动
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    toggleLock();
                } else if (!locked) {
                    mouseX = e.getX();
                    mouseY = e.getY();
                }
                if(e.getButton()==MouseEvent.BUTTON1&&false){
                    if(locked){
                    mouseX = e.getX();
                    mouseY = e.getY();
                    Robot robot=null;
                    try{
                        robot=new Robot();
                    }
                    catch (Exception ex){
                        ;
                    }
                    frame.setOpacity(0.0f);
                    robot.mousePress(InputEvent.BUTTON1_MASK);
                    frame.setOpacity(0.7f);
                }}

            }
        });

        frame.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!locked) {
                    int deltaX = e.getX() - mouseX;
                    int deltaY = e.getY() - mouseY;
                    frame.setLocation(frame.getX() + deltaX, frame.getY() + deltaY);
                }

            }
        });



        Temperature();
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTimeAndTemperature();
            }
        });
        timer.start();
        Timer timer0 = new Timer(30000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Temperature();
            }
        });
        timer0.start();

        // 设置窗口透明度
        frame.setOpacity(0.7f);

        frame.setVisible(true);
    }


    private void toggleLock() {
        locked = !locked;
        frame.setAlwaysOnTop(locked);
        if (locked) {
            frame.getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            frame.setAlwaysOnTop(true);

            //frame.setOpacity(0.5f);


        } else {
            frame.getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            frame.setOpacity(0.7f);

        }
    }
    private void setContentPaneBackground(JFrame frame, String imagePath) {
        try {
            // 从文件加载图片
            Image background = new ImageIcon(imagePath).getImage();

            // 将图片绘制到窗口的内容面板
            frame.setContentPane(new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTimeAndTemperature() {
        // 获取当前时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String currentTime = dateFormat.format(new java.util.Date());

        // 获取温度（假设从Python爬虫获取）
        //String temperature = getTemperatureFromPython0();

        // 更新标签文本
        timeLabel.setText(currentTime);//timeLabel.setText("Current Time: " + currentTime);
        //temperatureLabel.setText(temperature + "°C");//temperatureLabel.setText("Temperature: " + temperature + "°C");
    }
    private void Temperature() {
        // 获取当前时间
        //SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        //String currentTime = dateFormat.format(new java.util.Date());

        // 获取温度（假设从Python爬虫获取）
        String temperature = getTemperatureFromPython0();

        // 更新标签文本
        //timeLabel.setText(currentTime);//timeLabel.setText("Current Time: " + currentTime);
        temperatureLabel.setText(temperature + "°C");//temperatureLabel.setText("Temperature: " + temperature + "°C");
    }

    private String getTemperatureFromPython() {
        // TODO: 调用Python爬虫获取温度数据
        // 这里可以使用Java的ProcessBuilder来调用Python脚本
        return "25"; // 假设温度为25摄氏度
    }
    private String getTemperatureFromPython0() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "E://app/calendar/src/weather.py");
            Process process = processBuilder.start();

            // 获取Python脚本的输出
            java.io.InputStream inputStream = process.getInputStream();
            // 使用 InputStreamReader 指定 UTF-8 编码
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "GBK");

            // 使用 BufferedReader 读取文本
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            //java.util.Scanner scanner = new java.util.Scanner(inputStream).useDelimiter("\\A");
            //String result = scanner.hasNext() ? scanner.next() : "";
            String line;
            /*while ((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
            }*/
            line = bufferedReader.readLine();
            String result=line;
            // 关闭资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();


            // 等待Python脚本执行完成
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                return result.trim();
            } else {
                System.out.println(exitCode);
                System.err.println("Error running Python script");
                return "N/A";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "N/A";
        }
    }
}
