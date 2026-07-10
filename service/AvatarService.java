package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);
    private static final int BUFFER_SIZE = 1024;
    private static final String DOT = ".";

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    public AvatarService(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }

    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Was invoked method for upload avatar");
        logger.debug("Uploading avatar for student ID: {}", studentId);

        Student student = studentService.readStudent(studentId);
        Path filePath = Path.of(avatarsDir, studentId + DOT + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE);
                BufferedOutputStream bos = new BufferedOutputStream(os, BUFFER_SIZE);
        ) {
            bis.transferTo(bos);
        } catch (IOException e) {
            logger.error("Error occurred while saving avatar file for student ID = " + studentId, e);
            throw e;
        }

        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);
    }

    public Avatar findAvatar(Long studentId) {
        logger.info("Was invoked method for find avatar");
        return avatarRepository.findByStudentId(studentId).orElseGet(() -> {
            logger.warn("Avatar for student ID = {} not found, returning new empty Avatar", studentId);
            return new Avatar();
        });
    }

    public Collection<Avatar> getAllAvatars(int page, int size) {
        logger.info("Was invoked method for get all avatars");
        PageRequest pageRequest = PageRequest.of(page, size);
        return avatarRepository.findAll(pageRequest).getContent();
    }

    private String getExtensions(String fileName) {
        logger.debug("Extracting extension from filename: {}", fileName);
        if (fileName == null) {
            logger.warn("Filename is null while getting extension");
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(DOT) + 1);
    }
}
