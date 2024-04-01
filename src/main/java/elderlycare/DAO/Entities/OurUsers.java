package elderlycare.DAO.Entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "ourusers")
public class OurUsers implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String password;
    private String role;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE") // Définition de la valeur par défaut pour archived
    private Boolean archive=false;
    private String phoneNumber;

    private Double longitude; // Ajouter le champ pour stocker la longitude
    private Double latitude; // Ajouter le champ pour stocker la latitude
    private String address;
    @Column(columnDefinition = "BOOLEAN DEFAULT NULL")
    private Boolean online;

    //MARIEMMM

    public boolean isOnline() {
        return online != null && online;
    }




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

}
