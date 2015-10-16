package ro.teamnet.ou.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.service.AccountService;
import ro.teamnet.ou.acl.aop.OUFilter;
import ro.teamnet.ou.mapper.AccountMapper;
import ro.teamnet.ou.service.OrganizationAccountService;
import ro.teamnet.ou.service.OrganizationService;
import ro.teamnet.ou.web.rest.dto.AccountDTO;
import ro.teamnet.ou.web.rest.dto.OrganizationDTO;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Radu.Hoaghe on 8/3/2015.
 */
@RestController
@RequestMapping("/app/rest/organization")
public class OrganizationResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationResource.class);

    @Inject
    AccountService accountService;

    @Inject
    private OrganizationService organizationService;

    @Inject
    private OrganizationAccountService organizationAccountService;

    @RequestMapping(value = "/save",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity<OrganizationDTO> updateOrganization(@RequestBody OrganizationDTO organizationDTO) {
        if (organizationDTO.getId() != null) {
            organizationDTO = organizationService.update(organizationDTO);
        } else {
            organizationDTO = organizationService.save(organizationDTO);
        }
        if (organizationDTO == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(organizationDTO, HttpStatus.OK);
    }


//    @RequestMapping(value = "/deleteOrganization",
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE,
//            method = RequestMethod.POST)
//    public void deleteOrganization(@RequestBody OrganizationDTO organizationDTO) {
//
//        organizationService.delete(organizationDTO);
//    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE)
    @Timed
    public void delete(@PathVariable("id") Long id) {
        log.debug("REST request to delete : {}", id);
        organizationService.delete(id);
    }

    @RequestMapping(value = "/getOrganizationById/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizationDTO> getById(@PathVariable Long id) {

        log.debug("REST request to get Organization by id : {}", id);
        return new ResponseEntity<OrganizationDTO>(organizationService.findOrganizationDTOById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/getAllOrganizations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @OUFilter(value = "ro.teamnet.ou.web.rest.dto.OrganizationDTO")
    public ResponseEntity<Set<OrganizationDTO>> getAll() {

        log.debug("REST request to get all: OrganizationDTOs");
        return new ResponseEntity<Set<OrganizationDTO>>(organizationService.getAllOrganizationDTOs(), HttpStatus.OK);
    }

    @RequestMapping(value = "/accounts/{orgId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @OUFilter(value = "ro.teamnet.ou.web.rest.dto.AccountDTO")
    public ResponseEntity<Set<AccountDTO>> getOrganizationAccounts(@PathVariable Long orgId) {
        log.debug("REST request to get all: Organization Accounts");
        return new ResponseEntity<Set<AccountDTO>>(organizationAccountService.getAccountsByOrgId(orgId), HttpStatus.OK);
    }

    @RequestMapping(value = "/availableAccounts/{orgId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @OUFilter(value = "ro.teamnet.ou.web.rest.dto.AccountDTO")
    public ResponseEntity<Set<AccountDTO>> getAvailableOrgAccounts(@PathVariable Long orgId) {
        log.debug("REST request to get all: Organization Accounts");
        return new ResponseEntity<>(organizationAccountService.getAvailableAccounts(), HttpStatus.OK);
    }

    @RequestMapping(value = "/accounts/{orgId}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addAccounts(@PathVariable Long orgId, @RequestBody Set<AccountDTO> accountDTOs) {
        log.debug("REST request to get all: Organization Accounts");
        organizationAccountService.createOrUpdateOrgAccounts(orgId, accountDTOs);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/account/getAll", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @OUFilter("ro.teamnet.ou.web.rest.dto.AccountDTO")
    public ResponseEntity<Set<AccountDTO>> getAllAccounts() {
        List<Account> accounts = accountService.findAll();

        Set<AccountDTO> accountDTOs = new HashSet<>();
        for (Account account : accounts) {
            accountDTOs.add(AccountMapper.toDTO(account));
        }

        return new ResponseEntity<>(accountDTOs, HttpStatus.OK);
    }

    @RequestMapping(value = "/account/getByLogin/{login:.+}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDTO> getAccountByLogin(@PathVariable String login) {
        Account account = accountService.findByLogin(login);
        return new ResponseEntity<>(AccountMapper.toDTO(account), HttpStatus.OK);
    }

    /**
     * Method returns all the rights associated with this account that are part of the OU module
     *
     * @return
     */
    @RequestMapping(value = "/account/getCurrentUserWithOuAuthorities", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ro.teamnet.bootstrap.web.rest.dto.AccountDTO> getCurrentUserWithOuAuthorities() {
        ro.teamnet.bootstrap.web.rest.dto.AccountDTO accountDTO = organizationService.getCurrentUserWithOuAuthorities();
        return new ResponseEntity<>(accountDTO, HttpStatus.OK);
    }
}
