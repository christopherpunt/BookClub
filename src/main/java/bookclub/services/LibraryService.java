package bookclub.services;

import bookclub.models.Library;
import bookclub.repositories.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryService {
    @Autowired
    LibraryRepository libraryDao;

    public Library createLibrary(Library library){
        return libraryDao.save(library);
    }

    public List<Library> getLibraries() {
        return libraryDao.findAll();
    }
}
