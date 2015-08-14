package ro.teamnet.ou.acl.domain;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ro.teamnet.ou.acl.service.OrganizationalUnitHierarchyFilterAdvice;
import ro.teamnet.ou.domain.neo.OrganizationalUnit;
import ro.teamnet.ou.mock.OuTestApplication;
import ro.teamnet.ou.repository.jpa.OUHierarchyTest2EntityRepository;
import ro.teamnet.ou.repository.jpa.OUHierarchyTestEntityRepository;
import ro.teamnet.ou.repository.neo.OrganizationalUnitNeoRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = OuTestApplication.class)
@WebAppConfiguration
@IntegrationTest
@ActiveProfiles("test")
public class OrganizationalUnitHierarchyFilterTest {

    @Inject
    private OUHierarchyTestEntityRepository repository;
    @Inject
    private OUHierarchyTest2EntityRepository repository2;
    @Inject
    private OrganizationalUnitHierarchyFilterAdvice advice;
    @Inject
    private OrganizationalUnitNeoRepository ouNeoRepository;

    @Test
    @Transactional
    public void testOrganizationalUnitHierarchyFilterForRootWithNoChildren() {
        long OWNER_OU_ID = -1000L;
        saveNewTestEntity(OWNER_OU_ID);
        saveNewTestEntity(OWNER_OU_ID);
        saveNewTestEntity(2L);
        saveNewTestEntity(3L);
        List<OUHierarchyTestEntity> all = repository.findAll();
        Assert.assertEquals(4, all.size());
        advice.setupOrganizationalUnitHierarchyFilter(Arrays.asList(OWNER_OU_ID));
        all = repository.findAll();
        Assert.assertEquals(2, all.size());
        for (OUHierarchyTestEntity testEntity : all) {
            Assert.assertEquals(OWNER_OU_ID, testEntity.getOwnerOrganizationalUnitId().longValue());
        }
    }

    private void saveNewTestEntity(long ownerOrganizationalUnitId) {
        OUHierarchyTestEntity test = new OUHierarchyTestEntity();
        test.setOwnerOrganizationalUnitId(ownerOrganizationalUnitId);
        repository.save(test);
    }

    @Test
    @Transactional
    public void testOrganizationalUnitHierarchyFilterForRootWithNoChildrenOnTwoEntities() {
        long OWNER_OU_ID = -1000L;
        saveNewTestEntity(OWNER_OU_ID);
        saveNewTestEntity(OWNER_OU_ID);
        saveNewTestEntity(2L);
        saveNewTestEntity(3L);
        saveNewTest2Entity(OWNER_OU_ID);
        saveNewTest2Entity(OWNER_OU_ID);
        saveNewTest2Entity(OWNER_OU_ID);
        saveNewTest2Entity(OWNER_OU_ID);
        saveNewTest2Entity(OWNER_OU_ID);
        saveNewTest2Entity(2L);
        saveNewTest2Entity(3L);
        List<OUHierarchyTestEntity> all = repository.findAll();
        Assert.assertEquals(4, all.size());
        List<OUHierarchyTest2Entity> all2 = repository2.findAll();
        Assert.assertEquals(7, all2.size());
        advice.setupOrganizationalUnitHierarchyFilter(Arrays.asList(OWNER_OU_ID));
        all = repository.findAll();
        all2 = repository2.findAll();
        Assert.assertEquals(2, all.size());
        for (OUHierarchyTestEntity testEntity : all) {
            Assert.assertEquals(OWNER_OU_ID, testEntity.getOwnerOrganizationalUnitId().longValue());
        }
        Assert.assertEquals(5, all2.size());
        for (OUHierarchyTest2Entity test2Entity : all2) {
            Assert.assertEquals(OWNER_OU_ID, test2Entity.getOwnerOrganizationalUnitId().longValue());
        }
    }

    private void saveNewTest2Entity(long ownerOrganizationalUnitId) {
        OUHierarchyTest2Entity test = new OUHierarchyTest2Entity();
        test.setOwnerOrganizationalUnitId(ownerOrganizationalUnitId);
        repository2.save(test);
    }

    @Test
    @Transactional
    public void testOrganizationalUnitHierarchyFilterForRootWithOneChild() {
        long ROOT_OU_ID = -1000L;
        long CHILD_OU_ID = -1100L;

        OrganizationalUnit rootOu = saveOuToNeo(ROOT_OU_ID);
        OrganizationalUnit childOu = saveOuToNeo(CHILD_OU_ID);
        childOu.setParent(rootOu);
        ouNeoRepository.save(childOu);

        saveNewTestEntity(ROOT_OU_ID);
        saveNewTestEntity(ROOT_OU_ID);
        saveNewTestEntity(CHILD_OU_ID);
        saveNewTestEntity(CHILD_OU_ID);
        saveNewTestEntity(CHILD_OU_ID);
        saveNewTestEntity(2L);
        saveNewTestEntity(3L);
        List<OUHierarchyTestEntity> all = repository.findAll();
        Assert.assertEquals(7, all.size());
        advice.setupOrganizationalUnitHierarchyFilter(Arrays.asList(ROOT_OU_ID));
        all = repository.findAll();
        Assert.assertEquals(5, all.size());
        for (OUHierarchyTestEntity testEntity : all) {
            Assert.assertTrue(testEntity.getOwnerOrganizationalUnitId() < 0);
        }
        ouNeoRepository.delete(rootOu);
        ouNeoRepository.delete(childOu);
    }

    private OrganizationalUnit saveOuToNeo(long jpaId) {
        OrganizationalUnit ou = new OrganizationalUnit();
        ou.setJpaId(jpaId);
        return ouNeoRepository.save(ou);
    }

    @Test
    @Transactional
    public void testOrganizationalUnitHierarchyFilterForRootWithMultipleDescendants() {
        long ROOT_OU_ID = -1000L;
        long CHILD1_OU_ID = -1100L;
        long GRANDCHILD1_OU_ID = -1110L;
        long GRANDCHILD2_OU_ID = -1120L;
        long CHILD2_OU_ID = -1200L;

        OrganizationalUnit rootOu = saveOuToNeo(ROOT_OU_ID);
        OrganizationalUnit child1Ou = saveOuToNeo(CHILD1_OU_ID);
        OrganizationalUnit child2Ou = saveOuToNeo(CHILD2_OU_ID);
        OrganizationalUnit grandchild1Ou = saveOuToNeo(GRANDCHILD1_OU_ID);
        OrganizationalUnit grandchild2Ou = saveOuToNeo(GRANDCHILD2_OU_ID);
        child1Ou.setParent(rootOu);
        ouNeoRepository.save(child1Ou);
        child2Ou.setParent(rootOu);
        ouNeoRepository.save(child2Ou);
        grandchild1Ou.setParent(child1Ou);
        ouNeoRepository.save(grandchild1Ou);
        grandchild2Ou.setParent(child1Ou);
        ouNeoRepository.save(grandchild2Ou);

        saveNewTestEntity(ROOT_OU_ID);
        saveNewTestEntity(ROOT_OU_ID);
        saveNewTestEntity(CHILD1_OU_ID);
        saveNewTestEntity(CHILD1_OU_ID);
        saveNewTestEntity(CHILD1_OU_ID);
        saveNewTestEntity(CHILD2_OU_ID);
        saveNewTestEntity(CHILD2_OU_ID);
        saveNewTestEntity(GRANDCHILD1_OU_ID);
        saveNewTestEntity(GRANDCHILD1_OU_ID);
        saveNewTestEntity(GRANDCHILD1_OU_ID);
        saveNewTestEntity(GRANDCHILD2_OU_ID);
        saveNewTestEntity(GRANDCHILD2_OU_ID);
        saveNewTestEntity(2L);
        saveNewTestEntity(3L);
        List<OUHierarchyTestEntity> all = repository.findAll();
        Assert.assertEquals(14, all.size());
        advice.setupOrganizationalUnitHierarchyFilter(Arrays.asList(ROOT_OU_ID));
        all = repository.findAll();
        Assert.assertEquals(12, all.size());
        for (OUHierarchyTestEntity testEntity : all) {
            Assert.assertTrue(testEntity.getOwnerOrganizationalUnitId() < 0);
        }
        ouNeoRepository.delete(rootOu);
        ouNeoRepository.delete(child1Ou);
        ouNeoRepository.delete(child2Ou);
        ouNeoRepository.delete(grandchild1Ou);
        ouNeoRepository.delete(grandchild2Ou);
    }

}