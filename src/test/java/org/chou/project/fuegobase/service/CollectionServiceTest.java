package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.FuegobaseApplication;
import org.chou.project.fuegobase.model.database.Collection;
import org.chou.project.fuegobase.repository.database.CollectionRepository;
import org.chou.project.fuegobase.utils.HashIdUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = FuegobaseApplication.class, properties = {
        "spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults=false",
        "spring.jpa.hibernate.ddl-auto=none"
})
@ActiveProfiles("test")
public class CollectionServiceTest {

    @Autowired
    private CollectionService collectionService;

    @MockBean
    private HashIdUtil hashIdUtil;

    @MockBean
    private CollectionRepository collectionRepository;

    private Collection c1, c2;

    private List<Collection> fakeCollectionList;

    @Before
    public void init() {
        c1 = new Collection();
        c1.setHashId("aaa");
        c1.setName("collection1");

        c2 = new Collection();
        c2.setHashId("bbb");
        c2.setName("collection2");

        fakeCollectionList = List.of(c1, c2);
    }

    @Test
    public void get_collections() {
        when(collectionRepository.getCollectionsByProjectId(anyLong())).thenReturn(fakeCollectionList);
        when(hashIdUtil.decoded(anyString())).thenReturn(123L);
        List<Collection> collectionList = collectionService.getCollections("123");

        assertEquals(2, collectionList.size());
        assertEquals("aaa", collectionList.get(0).getHashId());
        assertEquals("collection1", collectionList.get(0).getName());

        assertEquals("bbb", collectionList.get(1).getHashId());
        assertEquals("collection2", collectionList.get(1).getName());

    }
}
