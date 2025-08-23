package com.alfa.file_status_processor.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("file")
public class File {
    @Id
    private String id;
    private String status;

    public File() {}
    public File(String hash, String status) {
        this.id = hash;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
