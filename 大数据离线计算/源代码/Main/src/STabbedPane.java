import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;

public class STabbedPane extends JTabbedPane {
    private static final long serialVersionUID = 1L;
    private Vector<Boolean> closable;
    private boolean showClose;

    public STabbedPane() {
        super();
        initialize();
    }

    /**
     * 构造方法
     * @param arg0 该参数为true时，无论是否选中，都显示可关闭按钮，为false时，只有选中时才显示
     */
    public STabbedPane(boolean arg0) {
        super();
        showClose = arg0;
        initialize();
    }

    private void initialize() {
        closable = new Vector<Boolean>(0);
        setUI(new STabbedPaneUI());
    }

    /**
     * 加入组件
     * @param title 标题
     //* @param icon 图标
     * @param component 组件
     //* @param tip 提示信息
     * @param closable 是否可关闭
     */
    public void addTab(String title, Component component, boolean closable) {
        addTab(title, component);
        this.closable.add(closable);
    }

    /**
     * 移除组件
     * @param index 组件序号
     */
    public void removeTab(int index) {
        super.removeTabAt(index);
        closable.remove(index);
    }

    class STabbedPaneUI extends BasicTabbedPaneUI {
        private Rectangle[] closeRects = new Rectangle[0];

        public STabbedPaneUI() {
            super();
            addMouseListener(new MouseAdapter(){
                public void mousePressed(MouseEvent e) {
                    for (int i=0;i<getTabCount();i++) {
                        if (closeRects[i].contains(e.getPoint()) && closable.get(i)) {
                            removeTab(i);
                        }
                    }
                }
            });
        }

        protected void paintTab(Graphics g, int tabPlacement,
                                Rectangle[] rects, int tabIndex,
                                Rectangle iconRect, Rectangle textRect) {
            super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);
            if (closable.get(tabIndex) &&
                    (showClose || tabIndex == getSelectedIndex())) {
                paintClose(g, tabIndex);
            }
        }

        private void paintClose(Graphics g, int tabIndex) {
            g.setColor(Color.BLACK);
            g.drawLine(closeRects[tabIndex].x,
                    closeRects[tabIndex].y,
                    closeRects[tabIndex].x + closeRects[tabIndex].width,
                    closeRects[tabIndex].y + closeRects[tabIndex].height);
            g.drawLine(closeRects[tabIndex].x + closeRects[tabIndex].width,
                    closeRects[tabIndex].y,
                    closeRects[tabIndex].x,
                    closeRects[tabIndex].y + closeRects[tabIndex].height);
        }

        protected void layoutLabel(int tabPlacement,
                                   FontMetrics metrics, int tabIndex,
                                   String title, Icon icon,
                                   Rectangle tabRect, Rectangle iconRect,
                                   Rectangle textRect, boolean isSelected ) {
            textRect.x = textRect.y = iconRect.x = iconRect.y = 0;

            View v = getTextViewForTab(tabIndex);
            if (v != null) {
                tabPane.putClientProperty("html", v);
            }
            SwingUtilities.layoutCompoundLabel((JComponent) tabPane,
                    metrics, title, icon,
                    SwingUtilities.CENTER,
                    SwingUtilities.LEFT,
                    SwingUtilities.CENTER,
                    SwingUtilities.TRAILING,
                    tabRect,
                    iconRect,
                    textRect,
                    textIconGap);
            tabPane.putClientProperty("html", null);

            int xNudge = getTabLabelShiftX(tabPlacement, tabIndex, isSelected);
            int yNudge = getTabLabelShiftY(tabPlacement, tabIndex, isSelected);
            iconRect.x += xNudge+5;
            iconRect.y += yNudge;
            textRect.x += xNudge+5;
            textRect.y += yNudge;
        }

        @Override
        protected LayoutManager createLayoutManager() {
            return new TabbedPaneLayout();
        }

        @Override
        protected void assureRectsCreated(int tabCount) {
            super.assureRectsCreated(tabCount);
            int rectArrayLen = closeRects.length;
            if (tabCount != rectArrayLen ) {
                Rectangle[] tempRectArray = new Rectangle[tabCount];
                System.arraycopy(closeRects, 0, tempRectArray, 0,
                        Math.min(rectArrayLen, tabCount));
                closeRects = tempRectArray;
                for (int rectIndex = rectArrayLen; rectIndex < tabCount; rectIndex++) {
                    closeRects[rectIndex] = new Rectangle();
                }
            }
        }

        class TabbedPaneLayout extends BasicTabbedPaneUI.TabbedPaneLayout {
            @Override
            protected void calculateTabRects(int tabPlacement, int tabCount) {
                super.calculateTabRects(tabPlacement, tabCount);
                for (int i=0;i<tabCount;i++) {
                    closeRects[i].x = rects[i].x + rects[i].width - 12;
                    closeRects[i].y = rects[i].y + 6;
                    closeRects[i].width = 8;
                    closeRects[i].height = 8;
                }
            }
        }
    }
}