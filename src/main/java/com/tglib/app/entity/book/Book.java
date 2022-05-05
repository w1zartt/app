package com.tglib.app.entity.book;

import com.tglib.app.entity.Image;
import com.tglib.app.entity.user.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_russian")
    private String nameRussian;

    @Column(name = "name_english")
    private String nameEnglish;

    @Column(name = "author_name_russian")
    private String authorRussian;

    @Column(name = "author_name_english")
    private String authorEnglish;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @Column(name = "storage_url")
    private String storageUrl;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private User publisher;

}

