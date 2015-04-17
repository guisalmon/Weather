package android.weather.rob.org.weather.utility;

/**
 * Convenience class containing the icon resource and the title for an element in the navigation
 * drawer.
 */
public class DrawerItem {
    public final int icon;
    public final String title;

    public DrawerItem(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }
}
