package com.evistek.mediaserver.service;

import com.evistek.mediaserver.entity.Admin;
import com.evistek.mediaserver.entity.Authority;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/21.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql({"classpath:test-schema.sql", "classpath:test-admins-authorities-data.sql"})
public class AdminServiceTests {
    @Autowired
    private IAdminService adminService;

    @Autowired
    @Qualifier("RSAService")
    private IAuthService rsaService;

    @Autowired
    @Qualifier("DESService")
    private IAuthService desService;

    @Autowired
    private IAuthorityService authorityService;

    @Before
    public void setup() {

    }

    @Test
    public void findAdmin() {
        int result = this.adminService.findAdminNumber();
        assertThat(result).isEqualTo(6);

        List<Admin> admins = this.adminService.findAdmins();
        assertThat(admins).isNotNull();
        assertThat(admins.size()).isEqualTo(6);

        List<Admin> adminList = this.adminService.findAdmins(1, 3);
        assertThat(adminList).isNotNull();
        assertThat(adminList.size()).isEqualTo(3);

        List<Admin> adminList1 = this.adminService.findAdmins(2, 3);
        assertThat(adminList1).isNotNull();
        assertThat(adminList1.size()).isEqualTo(3);
        assertThat(adminList1.get(0).getUsername()).isEqualTo("yshi");

        List<Authority> authorities = this.authorityService.findAuthorities();
        assertThat(authorities).isNotNull();
        assertThat(authorities.size()).isEqualTo(5);

        Admin admin = this.adminService.findAdminById(1);
        assertThat(admin).isNotNull();
        assertThat(admin.getUsername()).isEqualTo("wqzhou");
        assertThat(admin.getPassword()).isEqualTo("9nyEW38DIiw=");
        assertThat(admin.getEmail()).isEqualTo("wqzhou@evistek.com");
        assertThat(admin.getAuthority()).isEqualTo("ROLE_ADMIN");
        assertThat(admin.isEnabled()).isTrue();

        Authority authority = this.authorityService.findAuthorityById(3);
        assertThat(authority).isNotNull();
        assertThat(authority.getUsername()).isEqualTo("wqzhou");
        assertThat(authority.getAuthority()).isEqualTo("ROLE_ADMIN");

        Admin admin1 = this.adminService.findAdminByName("wxzhang");
        assertThat(admin1).isNotNull();
        assertThat(admin1.getId()).isEqualTo(2);
        assertThat(admin1.getEmail()).isEqualTo("wxzhang@evistek.com");
        assertThat(admin1.getPassword()).isEqualTo("9nyEW38DIiw=");
        assertThat(admin1.getAuthority()).isEqualTo("ROLE_ADMIN");
        assertThat(admin1.isEnabled()).isTrue();

        Authority authority1 = this.authorityService.findAuthorityByUsername("wxzhang");
        assertThat(authority1).isNotNull();
        assertThat(authority1.getId()).isEqualTo(1);
        assertThat(authority1.getAuthority()).isEqualTo("ROLE_ADMIN");

        List<String> roles = this.adminService.findAdminRoles();
        assertThat(roles.size()).isEqualTo(7);
        assertThat(roles.get(0)).isEqualTo("ROLE_UPLOAD");
    }

    @Test
    public void addAdmin() {
        Admin admin = new Admin();
        admin.setUsername("newAdmin");
        admin.setPassword("newPassword");
        admin.setEmail("newadmin@evistek.com");
        admin.setEnabled(true);

        admin.setAuthority(
                IAdminService.ROLE_USER_MANAGER + "," +
                IAdminService.ROLE_DBA);

        Admin newAdmin = this.adminService.addAdmin(admin);
        assertThat(newAdmin).isNotNull();
        assertThat(newAdmin.getUsername()).isEqualTo("newAdmin");

        Admin addedAdmin = this.adminService.findAdminByName("newAdmin");
        assertThat(addedAdmin).isNotNull();
        assertThat(addedAdmin.getAuthority()).isEqualTo("ROLE_USER_MANAGER,ROLE_DBA");

        Authority authority = this.authorityService.findAuthorityByUsername("newAdmin");
        assertThat(authority).isNotNull();
        assertThat(authority.getAuthority()).isEqualTo("ROLE_USER_MANAGER,ROLE_DBA");
    }

    @Test
    public void deleteAdmin() {
        Admin admin = this.adminService.findAdminByName("wxzhang");
        assertThat(admin).isNotNull();
        Authority authority = this.authorityService.findAuthorityByUsername("wxzhang");
        assertThat(authority).isNotNull();
        int result = this.adminService.deleteAdmin(admin);
        assertThat(result).isEqualTo(1);
        Admin deletedAdmin = this.adminService.findAdminByName("wxzhang");
        assertThat(deletedAdmin).isNull();
//        Authority deletedAuthority = this.authorityService.findAuthorityByUsername("wxzhang");
//        System.out.println("deletedAuthority " + deletedAuthority.getId() + " "
//                + deletedAuthority.getUsername() + " "
//                + deletedAuthority.getAuthority());
//        assertThat(deletedAuthority).isNull();

        Admin admin1 = this.adminService.findAdminById(1);
        assertThat(admin1).isNotNull();
        Authority authority1 = this.authorityService.findAuthorityById(1);
        assertThat(authority1).isNotNull();
        int result1 = this.adminService.deleteAdminById(1);
        assertThat(result1).isEqualTo(1);
        Admin deletedAdmin1 = this.adminService.findAdminById(1);
        assertThat(deletedAdmin1).isNull();
//        Authority deletedAuthority1 = this.authorityService.findAuthorityById(1);
//        System.out.println("deletedAuthority1 " + deletedAuthority1.getId() + " "
//                + deletedAuthority1.getUsername() + " "
//                + deletedAuthority1.getAuthority());
//        assertThat(deletedAuthority1).isNull();

        Admin admin2 = this.adminService.findAdminByName("yshi");
        assertThat(admin2).isNotNull();
        Authority authority2 = this.authorityService.findAuthorityByUsername("yshi");
        assertThat(authority2).isNotNull();
        int result2 = this.adminService.deleteAdminByName("yshi");
        assertThat(result2).isEqualTo(1);
        Admin deletedAdmin2 = this.adminService.findAdminByName("yshi");
        assertThat(deletedAdmin2).isNull();
//        Authority deletedAuthority2 = this.authorityService.findAuthorityByUsername("yshi");
//        System.out.println("deletedAuthority2 " + deletedAuthority2.getId() + " "
//                + deletedAuthority2.getUsername() + " "
//                + deletedAuthority2.getAuthority());
//        assertThat(deletedAuthority2).isNull();
    }

    @Test
    public void updateAdmin() {
        Admin admin = this.adminService.findAdminByName("wxzhang");
        assertThat(admin).isNotNull();
        assertThat(admin.getAuthority()).isEqualTo("ROLE_ADMIN");
        Authority authority = this.authorityService.findAuthorityByUsername("wxzhang");
        assertThat(authority).isNotNull();
        assertThat(authority.getAuthority()).isEqualTo("ROLE_ADMIN");

        RSAPublicKey publicKey = this.rsaService.getPublicKey();
        String password = this.rsaService.encryptToBase64String("newpassword", publicKey);
        admin.setPassword(password);
        admin.setEmail("newemail@evistek.com");
        admin.setAuthority("ROLE_USER");
        admin.setEnabled(false);
        int result = this.adminService.updateAdmin(admin);
        assertThat(result).isEqualTo(1);

        Admin newAdmin = this.adminService.findAdminByName("wxzhang");
        assertThat(newAdmin).isNotNull();

        try {
            String ps = new String(
                    this.desService.decryptFromBase64String(newAdmin.getPassword(), this.desService.getKeyForPassword()),
                    "UTF-8");
            assertThat(ps).isEqualTo("newpassword");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        assertThat(newAdmin.getEmail()).isEqualTo("newemail@evistek.com");
        assertThat(newAdmin.getAuthority()).isEqualTo("ROLE_USER");
        assertThat(newAdmin.isEnabled()).isFalse();

        Authority newAuthority = this.authorityService.findAuthorityByUsername("wxzhang");
        assertThat(newAuthority).isNotNull();
        assertThat(newAuthority.getAuthority()).isEqualTo("ROLE_USER");
    }
}
