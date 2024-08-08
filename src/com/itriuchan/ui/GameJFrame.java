package com.itriuchan.ui;

import cn.hutool.core.io.IoUtil;
import com.itriuchan.bean.GameInfo;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
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

    JMenu saveJMenu = new JMenu("存档");
    JMenu loadJMenu = new JMenu("读档");

    JMenuItem saveItem0 = new JMenuItem("存档0(空)");
    JMenuItem saveItem1 = new JMenuItem("存档1(空)");
    JMenuItem saveItem2 = new JMenuItem("存档2(空)");
    JMenuItem saveItem3 = new JMenuItem("存档3(空)");
    JMenuItem saveItem4 = new JMenuItem("存档4(空)");

    JMenuItem loadItem0 = new JMenuItem("读档0(空)");
    JMenuItem loadItem1 = new JMenuItem("读档1(空)");
    JMenuItem loadItem2 = new JMenuItem("读档2(空)");
    JMenuItem loadItem3 = new JMenuItem("读档3(空)");
    JMenuItem loadItem4 = new JMenuItem("读档4(空)");
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

        //把5个存档，添加到saveJMenu中
        saveJMenu.add(saveItem0);
        saveJMenu.add(saveItem1);
        saveJMenu.add(saveItem2);
        saveJMenu.add(saveItem3);
        saveJMenu.add(saveItem4);

        //把5个读档，添加到loadJMenu中
        loadJMenu.add(loadItem0);
        loadJMenu.add(loadItem1);
        loadJMenu.add(loadItem2);
        loadJMenu.add(loadItem3);
        loadJMenu.add(loadItem4);

        functionJMenu.add(changeImage);

        functionJMenu.add(saveJMenu);
        functionJMenu.add(loadJMenu);

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

        saveItem0.addActionListener(this);
        saveItem1.addActionListener(this);
        saveItem2.addActionListener(this);
        saveItem3.addActionListener(this);
        saveItem4.addActionListener(this);
        loadItem0.addActionListener(this);
        loadItem1.addActionListener(this);
        loadItem2.addActionListener(this);
        loadItem3.addActionListener(this);
        loadItem4.addActionListener(this);

        //将菜单里面的两个选项添加到菜单当中
        jMenuBar.add(functionJMenu);
        jMenuBar.add(contactJmenu);

        //读取存档信息，修改菜单上表示的内容
        getGameInfo();

        this.setJMenuBar(jMenuBar);
    }

    public void getGameInfo(){
        //1.创建File对象表示所有存档所在的文件夹
        File file = new File("save");
        //2.进入文件夹获取到里面所有的存档文件
        File[] files = file.listFiles();
        //3.遍历数组，得到每一个存档
        for (File f : files) {
            //f ：依次表示每一个存档文件
            //获取每一个存档文件中的步数
            GameInfo gi = null;
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
                gi = (GameInfo)ois.readObject();
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            //获取到了步数
            int step = gi.getStep();

            //把存档的步数同步到菜单当中
            //save0 ---> 0
            //save1 ---> 1
            //...

            //获取存档的文件名 save0.data
            String name = f.getName();
            //获取当存档的序号（索引）
            int index = name.charAt(4) - '0';
            //修改菜单上所表示的文字信息
            saveJMenu.getItem(index).setText("存档" + index + "(" + step + ")步");
            loadJMenu.getItem(index).setText("存档" + index + "(" + step + ")步");
        }

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
        else if (object == saveItem0 || object == saveItem1 || object == saveItem2 || object == saveItem3 || object == saveItem4) {
            //获取当前是哪个存档被点击了，获取其中的序号
            JMenuItem item = (JMenuItem) object;
            String str = item.getText();
            int index = str.charAt(2) - '0';

            //直接把游戏的数据写到本地文件中
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save\\save" + index + ".data"));
                GameInfo gi = new GameInfo(data, x, y, path, step);
                IoUtil.writeObj(oos, true, gi);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            //修改一下存档item上的展示信息
            //存档0(XX步)
            item.setText("存档" + index + "(" + step + "步)");
            //修改一下读档item上的展示信息
            loadJMenu.getItem(index).setText("存档" + index + "(" + step + "步)");
        } else if (object == loadItem0 || object == loadItem1 || object == loadItem2 || object == loadItem3 || object == loadItem4) {
            //获取当前是哪个读档被点击了，获取其中的序号
            JMenuItem item = (JMenuItem) object;
            String str = item.getText();
            int index = str.charAt(2) - '0';

            GameInfo gi = null;
            try {
                //可以到本地文件中读取数据
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("save\\save" + index + ".data"));
                gi = (GameInfo)ois.readObject();
                ois.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }

            data = gi.getData();
            path = gi.getPath();
            step = gi.getStep();
            x = gi.getX();
            y = gi.getY();

            //重新刷新界面加载游戏
            initImage();

        }
    }
}
