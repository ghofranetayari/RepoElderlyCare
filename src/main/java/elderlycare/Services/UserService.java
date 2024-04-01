package elderlycare.Services;

import elderlycare.DAO.Entities.OurUsers;
import elderlycare.DAO.Repositories.OurUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private OurUserRepo ourUserRepo;

    @Autowired
    public UserService(OurUserRepo userRepository) {
        this.ourUserRepo = userRepository;
    }


    public int ArchiveUser(Integer Id) {
        OurUsers ourUsers = ourUserRepo.findById(Id).get();
        if (ourUsers != null) {
            ourUsers.setArchive(true);
            ourUserRepo.save(ourUsers);

            return 1;
        } else {
            return 0;
        }

    }

    public int ActivateUser(Integer Id) {
        OurUsers ourUsers = ourUserRepo.findById(Id).get();
        if (ourUsers != null) {
            ourUsers.setArchive(false);
            ourUserRepo.save(ourUsers);


            return 1;
        } else {
            return 0;
        }

    }}



