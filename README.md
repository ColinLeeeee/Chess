# Chess
A ♞chess GUI in JavaFx.
Note that's my first project which serves as a assignment for Java Programming Course, not a elegant and efficient one.

## 概况
可以进行人机对战的国际象棋程序，GUI利用Javafx绘制，核心操作基于Javafx的DragEvent的处理而进行。实现国际象棋走子，吃子的基本规则和兵升变等特殊规则。

本程序的电脑方只实现了依照规则走子，并未加入进一步的算法。

<center>
<img src="https://lh3.googleusercontent.com/-PI9QQ6N8GOw/WxgDqreWXtI/AAAAAAAAAHU/u_vb9PEmoQ8xkYvAZWNgjn97gTuzJ5F7gCHMYCw/I/15283004557442.jpg" width="75%" height="25%" />
棋盘和棋子的GUI
</center>

##操作
###基本操作
* 程序运行时弹出对话框，选择开局方；

<center>
<img src="https://lh3.googleusercontent.com/-Xq_b90coqaw/WxgDmfDBhZI/AAAAAAAAAHQ/t0o8JZLlCpgcOxPdigqesOu1lvkpvSRZwCHMYCw/I/15283004389488.jpg" width="75%" height="25%" />
选择开局方的对话框
</center>

* 鼠标拖拽棋子到目标位置并放开，从而实现走子；
    * 拖拽棋子通过一格时，该格会通过颜色变化给予可视化反馈，若该处可以走子，显示绿色；若不可以走子，显示红色。
    
###王车易位Castling
* 按下棋盘右下按钮进行王车易位，若不符合王车易位条件，程序弹出对话框提示；
* 若符合条件，程序弹出对话框，提示选择长易位或短易位；

###兵升变Promotion
* 兵到达底线后，程序弹出对话框提示选择升变的角色


<center>
<img src="https://lh3.googleusercontent.com/-VbDB83_EIWc/WxgFfU9urAI/AAAAAAAAAHk/N2pSHK-pUiYEJ7FdzgwKFKcKrHaAKNZPQCHMYCw/I/15283009234469.jpg" width="75%" height="25%" />
兵升变对话框
</center>

###意大利开局
由于要求实现意大利开局，电脑方开局必定为意大利开局走法，用户应同样进行意大利开局。**若该过程中不依据意大利开局吃掉电脑方的特定子，程序可能报错。**本程序依据的意大利开局模式见参考网站。



##记录棋谱
实现记录棋谱到一个文本文件，命名为`ScoreBook_程序运开始运行时的日期与时间.txt`，若目录不存在，程序会在jar文件同一目录下创建`ScoreBook`目录，棋谱文件保存在该目录下。项目目录下有一个棋谱示例文件： `ScoreBook_2018_6_8_1/26.txt`

棋谱示例：

```
2018_6_8_1:26:42
1.4E-5E
2.N3F-N6C
3.B4C-B5C
4.3C-N6F
5.4D-4D
6.4D-B4B
7.B2D-B2D
8.N2D-5D
9.5D-N5D
10.Q3B-N7E
11.Castling: K1G-Castling: K8G
12.R1E-N5F
13.R8E-R8E
14.R1B-R3E
15.N5E-R2E
16.4F-R2D
17.K1F-N4F
18.R1A-N3H
19.5D-N3E
20.6D-6D
21.K1E-R2G
22.4A-R1G
23.Q7B-B7B
24.N7D-Q7D
25.4B-R1E
black wins
```

>注：棋谱只有在该局正常结束的情况下才能正常记录，程序强制退出的情况下不会记录。

##开发及编译环境


>IntelliJ IDEA 2018.1.2 (Community Edition)
Build #IC-181.4668.68, built on April 24, 2018
JRE: 1.8.0_152-release-1136-b29 x86_64
JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
macOS 10.13.3



已打包为jar文件，在`Chess_Limenghao/out/artifacts/Chess_Limenghao_jar`目录下。可在命令行界面输入`java -jar Chess_Limenghao.jar`运行程序。如下图所示： 

![](https://lh3.googleusercontent.com/-lblACazWFAc/Wx6PQb56V3I/AAAAAAAAAH4/d_5OeHR_zQwd9nN3cctrq6bc7-s-CTc2QCHMYCw/I/15287294072407.jpg)


### Reference
1. [DOC-07-07 拖放操作 | JavaFX、OSGi、Eclipse开源资料](http://www.javafxchina.net/blog/2015/09/doc0707-drag-drop/)
2. [国际象棋意大利开局图文详解](https://wenku.baidu.com/view/0fa0132c4a73f242336c1eb91a37f111f1850db0.html)
3. [国际象棋的简易代数记谱法](http://blog.sina.com.cn/s/blog_621c4ed80101da18.html)
4. [王车易位规则](http://blog.sina.com.cn/s/blog_99a358500101f1pl.html)
