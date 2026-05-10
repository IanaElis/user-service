package com.alex.project.service;

//import com.alex.project.entity.Alumni;
import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.inject.Inject;
//import jakarta.persistence.EntityManager;
//import org.hibernate.search.mapper.orm.Search;
//import org.hibernate.search.mapper.orm.session.SearchSession;
//
//import java.util.List;

@ApplicationScoped
public class UserSearchService {

//    @Inject
//    EntityManager em;
//
//    public List<Alumni> searchAlumni(String search) {
//        SearchSession searchSession =
//                Search.session(em);
//
//        return searchSession.search(Alumni.class)
//                .where(f -> f.bool()
//                        .must(f.match()
//                                .fields("firstName", "lastName")
//                                .matching(search))
//                        .filter(f.match()
//                                .field("verified")
//                                .matching(true)))
//                .fetchHits(20);
//    }


}
