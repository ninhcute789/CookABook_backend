package NandK.CookABook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import NandK.CookABook.entity.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {
    public Author findByName(String name);

    public boolean existsByName(String name);

    public List<Author> findByIdIn(List<Long> ids); // lay ra nhieu tac gia theo list id

}
