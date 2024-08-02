package com.itriuchan.ui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GameJFrame extends JFrame implements KeyListener, ActionListener {

    //创建随机数
    Random random = new Random();
    //根据二维数组加载图片
    int [][] data = new int[4][4];
    //记录空白块在数组中的位置
    int x = 0;
    int y =0;
    //存储正确数组顺序
    int[][] win = {
            {1,2,3,4},
            {5,6,7,8},
            {9,10,11,12},
            {13,14,15,0},
    };
    JMenuItem replayItem = new JMenuItem("重新游戏");
    JMenuItem reLoginItem = new JMenuItem("注销登陆");
    JMenuItem closeItem = new JMenuItem("关闭游戏");
    JMenuItem contactItem = new JMenuItem("邮箱");
    JMenuItem girl = new JMenuItem("美女");
    JMenuItem animal = new JMenuItem("动物");
    JMenuItem sport = new JMenuItem("运动");
    //统计步数
    int step = 0;
    //记录文件路径
    String path = "image";
    String girlPath = "image\\girl\\";
    String animalPath = "image\\animal";
    String sportPath = "image\\sport";
    public GameJFrame(){
        //初始化界面参数
        initJFrame();

        //初始化菜单
        initJMenuBar();

        //初始化数据
        initData();

        //初始化图片
        int index = random.nextInt(13)+1;
        path = girlPath + "\\girl"+index+"\\";
        initImage();

        this.setVisible(true);
    }

    private void initData() {
        int[] tempArr = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};

        for (int i = 0; i < tempArr.length; i++) {
            int index = random.nextInt(tempArr.length);
            int temp = tempArr[i];
            tempArr[i] = tempArr[index];
            tempArr[index] = temp;
        }
        for (int i = 0; i < tempArr.length; i++) {
            if(tempArr[i] == 0){
                x = i / 4;
                y = i % 4;
            }
            data[i / 4][i % 4] = tempArr[i];
        }
    }

    private void initImage() {
        //清空已经出现的图片
        this.getContentPane().removeAll();

        if(victory()){
            JLabel winJLabel = new JLabel(new ImageIcon("image\\win.png"));
            winJLabel.setBounds(203,283,197,73);
            this.getContentPane().add(winJLabel);
        }

        JLabel stepCount = new JLabel("步数:" + step);
        stepCount.setBounds(50,30,100,20);
        this.getContentPane().add(stepCount);

        //加载图片
        for (int i = 0;i < 4;i++) {
            for (int j =0;j < 4;j++) {
                int number = data[i][j];
                JLabel jLabel = new JLabel(new ImageIcon(path+number+".jpg"));
                jLabel.setBounds(105*j + 83,105*i + 134,105,105);
                jLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
                this.getContentPane().add(jLabel);
            }
        }

        //添加背景
        JLabel background = new JLabel(new ImageIcon("image\\background.png"));
        background.setBounds(40,40,508,560);
        this.getContentPane().add(background);

        //刷新界面
        this.getContentPane().repaint();

    }

    private void initJMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();

        JMenu functionJMenu = new JMenu("功能");
        JMenu contactJmenu = new JMenu("联系我");
        JMenu changeImage = new JMenu("更换图片");

        changeImage.add(girl);
        changeImage.add(animal);
        changeImage.add(sport);
        functionJMenu.add(changeImage);
        functionJMenu.add(replayItem);
        functionJMenu.add(reLoginItem);
        functionJMenu.add(closeItem);
        contactJmenu.add(contactItem);

        //绑定事件
        replayItem.addActionListener(this);
        closeItem.addActionListener(this);
        contactItem.addActionListener(this);
        reLoginItem.addActionListener(this);

        girl.addActionListener(this);
        animal.addActionListener(this);
        sport.addActionListener(this);

        jMenuBar.add(functionJMenu);
        jMenuBar.add(contactJmenu);

        this.setJMenuBar(jMenuBar);
    }

    private void initJFrame() {
        this.setSize(603,680);
        this.setTitle("拼图游戏青春版");
        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //取消默认居中
        this.setLayout(null);
        this.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(victory()){
            return;
        }
        //A键查看完整图片
        if (e.getKeyCode() == 65){
            this.getContentPane().removeAll();
            JLabel complete = new JLabel(new ImageIcon(path+"\\all.jpg") );
            complete.setBounds(83,134,420,420);
            this.getContentPane().add(complete);

            //添加背景
            JLabel background = new JLabel(new ImageIcon("image\\background.png"));
            background.setBounds(40,40,508,560);
            this.getContentPane().add(background);
            //刷新
            this.getContentPane().repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(victory()){
            return;
        }
        //左37，上38，右39，下40,A65,W87
        switch (e.getKeyCode()){
            case 37:
                if (y == 0){
                    return;
                }
                data[x][y] = data[x][y - 1];
                data[x][y - 1] = 0;
                y--;
                step++;
                initImage();
                break;
            case 38:
                if (x == 0){
                    return;
                }
                data[x][y] = data[x - 1][y];
                data[x - 1][y] = 0;
                x--;
                step++;
                initImage();
                break;
            case 39:
                if (y == 3){
                    return;
                }
                data[x][y] = data[x][y + 1];
                data[x][y + 1] = 0;
                y++;
                step++;
                initImage();
                break;
            case 40:
                if (x == 3){
                    return;
                }
                data[x][y] = data[x + 1][y];
                data[x + 1][y] = 0;
                x++;
                step++;
                initImage();
                break;
            case 65:
                initImage();
                break;
            case 87:
                data = new int[][]{
                        {1,2,3,4},
                        {5,6,7,8},
                        {9,10,11,12},
                        {13,14,15,0},
                };
                initImage();
        }
    }
    public boolean victory(){
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] != win[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object object = e.getSource();
        if(object == replayItem){
            step = 0;
            initData();
            initImage();
        }
        else if (object == closeItem){
            System.exit(0);
        }
        else if (object == reLoginItem){
            this.setVisible(false);
            new LoginJFrame();
        }
        else if (object == contactItem){
            JDialog jDialog = new JDialog();
            JLabel jLabel = new JLabel(new ImageIcon("image\\contact.png"));
            jLabel.setBounds(0,0,258,258);
            jDialog.getContentPane().add(jLabel);
            jDialog.setSize(344,344);
            jDialog.setAlwaysOnTop(true);
            jDialog.setLocationRelativeTo(null);
            jDialog.setModal(true);
            jDialog.setVisible(true);
        }
        else if (object == girl){
            step = 0;
            initData();
            int index = random.nextInt(13)+1;
            path = girlPath + "\\girl"+index+"\\";
            initImage();
        }
        else if (object == animal){
            step = 0;
            initData();
            int index = random.nextInt(8)+1;
            path = animalPath + "\\animal"+index+"\\";
            initImage();
        }
        else if (object == sport){
            step = 0;
            initData();
            int index = random.nextInt(10)+1;
            path = sportPath + "\\sport"+index+"\\";
            initImage();
        }
    }
}
