package wallpaper.deekshithrajbasa.com.wall_papers;


public class instagramAdapter {
    private String imageUrl;
    public instagramAdapter() {
    }
    public instagramAdapter( String imageUrl) {

        this.imageUrl = imageUrl;
    }

    public String getImage() {
        return imageUrl;
    }
    public void setImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
