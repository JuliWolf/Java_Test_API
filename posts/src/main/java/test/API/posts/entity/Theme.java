package test.API.posts.entity;

import javax.persistence.*;

@Entity
@Table(name="themes")
public class Theme {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id")
  private int id;

  @Column(name="name")
  private String name;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Theme() {
  }

  public Theme(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Theme{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
