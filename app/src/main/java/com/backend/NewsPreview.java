package com.backend;

import java.util.List;

public class NewsPreview extends News {
    private String previewContent;
    private String thumbnailURL;
    public NewsPreview() {
        super();
    }

    public NewsPreview(String title, String type, String authorUsername, String previewContent, String thumbnailURL) {
        this.previewContent = previewContent;
        this.thumbnailURL = thumbnailURL;
        super.authorUsername = authorUsername;
        super.type = type;
        super.title = title;
    }

    // Getter
    public String getPreviewContent() {
        return previewContent;
    }

    public void setPreviewContent(String previewContent) {
        this.previewContent = previewContent;
    }

    // Setter
    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}
