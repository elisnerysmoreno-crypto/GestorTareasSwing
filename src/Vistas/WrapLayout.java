
package Vistas;

import java.awt.*;

/**
 * Un Layout Manager que simula un FlowLayout pero se envuelve (wrap)
 * a la siguiente línea cuando no hay espacio, sin estirar los componentes.
 */
public class WrapLayout extends FlowLayout {

    public WrapLayout() {
        super();
    }

    public WrapLayout(int align) {
        super(align);
    }

    public WrapLayout(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        return layoutSize(target, true);
    }

    @Override
    public Dimension minimumLayoutSize(Container target) {
        Dimension minimum = layoutSize(target, false);
        minimum.width -= (getHgap() + 1);
        return minimum;
    }

 
    private Dimension layoutSize(Container target, boolean preferred) {
        synchronized (target.getTreeLock()) {
            int targetWidth = target.getSize().width;
            
            if (targetWidth == 0) {
                targetWidth = Integer.MAX_VALUE;
            }

            int hgap = getHgap();
            int vgap = getVgap();
            Insets insets = target.getInsets();
            int maxWidth = targetWidth - (insets.left + insets.right + hgap * 2);

            int width = 0;
            int height = insets.top + vgap;

            int rowWidth = 0;
            int rowHeight = 0;

            int nMembers = target.getComponentCount();
            boolean firstComponent = true;

            for (int i = 0; i < nMembers; i++) {
                Component m = target.getComponent(i);

                if (m.isVisible()) {
                    Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                    
                    // Empieza una nueva línea si no hay espacio para el componente
                    if (!firstComponent && rowWidth + d.width > maxWidth) {
                        width = Math.max(width, rowWidth);
                        height += rowHeight + vgap;
                        rowWidth = 0;
                        rowHeight = 0;
                        firstComponent = true;
                    }
                    
                    // Si es el primer componente de la línea
                    if (firstComponent) {
                        rowWidth = d.width;
                        rowHeight = d.height;
                        firstComponent = false;
                    } else {
                        rowWidth += hgap + d.width;
                        rowHeight = Math.max(rowHeight, d.height);
                    }
                }
            }
            
            // Añadir la última fila
            width = Math.max(width, rowWidth);
            height += rowHeight + insets.bottom + vgap;

            return new Dimension(width + insets.left + insets.right + hgap * 2, height);
        }
    }
}
