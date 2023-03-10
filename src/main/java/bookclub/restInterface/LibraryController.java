package bookclub.restInterface;

import bookclub.library.Library;
import bookclub.library.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LibraryController {
    @Autowired
    LibraryService libraryService;

    @RequestMapping(value = "/library", method= RequestMethod.POST)
    public Library createLibrary(@RequestBody Library library){
        return libraryService.createLibrary(library);
    }

    @RequestMapping(value = "/libraries", method=RequestMethod.GET)
    public List<Library> getLibraries(){
        return libraryService.getLibraries();
    }

}
