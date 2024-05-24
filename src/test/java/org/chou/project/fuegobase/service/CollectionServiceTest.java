package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.model.database.Collection;
import org.chou.project.fuegobase.repository.database.CollectionRepository;
import org.chou.project.fuegobase.service.impl.CollectionServiceImpl;
import org.chou.project.fuegobase.utils.HashIdUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CollectionServiceTest {

    @InjectMocks
    private CollectionServiceImpl collectionService;

    @Mock
    private HashIdUtil hashIdUtil;

    @Mock
    private CollectionRepository collectionRepository;

    private Collection c1, c2;

    private List<Collection> fakeCollectionList;

    @BeforeEach
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
