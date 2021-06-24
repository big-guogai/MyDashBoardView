# MyDashBoardView
自定义仪表盘式进度条

该自定义view未实现padding属性
使用方法：

            <com.thinkwithu.www.gre.ui.widget.MyDashBoardView
                android:id="@+id/dash_board"
                android:layout_width="match_parent"
                android:layout_height="@dimen/dp_200"
                android:layout_marginStart="@dimen/dp_20"
                android:layout_marginTop="@dimen/dp_20"
                android:layout_marginEnd="@dimen/dp_20" />


在代码中

        dash_board.bottomText = "模考分数"//底部文本
        dash_board.max = 170f//进度条最大值
        dash_board.setAnimaion(if (data.data.score != null) data.data.score.toFloat() else 0f)//设置动画
        //其他属性参照view文件设置
