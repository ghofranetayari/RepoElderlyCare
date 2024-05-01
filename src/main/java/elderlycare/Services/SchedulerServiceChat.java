package elderlycare.Services;

import elderlycare.DAO.Entities.Elderly;
import elderlycare.DAO.Entities.Nurse;
import elderlycare.DAO.Repositories.NurseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SchedulerServiceChat {

    @Autowired
    private NurseRepository nurseRepository;

    @Autowired
    private ChatMessageService messageService;

    @Transactional
    public List<Nurse> getAllNursesWithElderly() {
        return nurseRepository.findAll();
    }

    @Transactional
    @Scheduled(cron = "0 0 8 * * *")
    public void sendMorningMessages() {
        List<Nurse> nurses = getAllNursesWithElderly();
        for (Nurse nurse : nurses) {
            for (Elderly elderly : nurse.getElderlys()) {
                messageService.sendMorningMessageToElderly(nurse, elderly);
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 20 * * *")
    public void sendEveningMessages() {
        List<Nurse> nurses = getAllNursesWithElderly();
        for (Nurse nurse : nurses) {
            for (Elderly elderly : nurse.getElderlys()) {
                messageService.sendEveningMessageToElderly(nurse, elderly);
            }
        }
    }
}
