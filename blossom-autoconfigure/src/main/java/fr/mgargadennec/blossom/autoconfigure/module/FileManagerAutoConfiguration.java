package fr.mgargadennec.blossom.autoconfigure.module;

import fr.mgargadennec.blossom.core.common.utils.privilege.Privilege;
import fr.mgargadennec.blossom.core.common.utils.privilege.SimplePrivilege;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.Client;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import fr.mgargadennec.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.mgargadennec.blossom.core.common.search.IndexationEngineImpl;
import fr.mgargadennec.blossom.core.common.search.SearchEngineImpl;
import fr.mgargadennec.blossom.module.filemanager.File;
import fr.mgargadennec.blossom.module.filemanager.FileContentDao;
import fr.mgargadennec.blossom.module.filemanager.FileContentDaoImpl;
import fr.mgargadennec.blossom.module.filemanager.FileContentRepository;
import fr.mgargadennec.blossom.module.filemanager.FileDTO;
import fr.mgargadennec.blossom.module.filemanager.FileDTOMapper;
import fr.mgargadennec.blossom.module.filemanager.FileDao;
import fr.mgargadennec.blossom.module.filemanager.FileDaoImpl;
import fr.mgargadennec.blossom.module.filemanager.FileIndexationJob;
import fr.mgargadennec.blossom.module.filemanager.FileRepository;
import fr.mgargadennec.blossom.module.filemanager.FileService;
import fr.mgargadennec.blossom.module.filemanager.FileServiceImpl;
import fr.mgargadennec.blossom.module.filemanager.digest.DigestUtil;
import fr.mgargadennec.blossom.module.filemanager.digest.DigestUtilImpl;

/**
 * Created by Maël Gargadennnec on 19/05/2017.
 */
@Configuration
@ConditionalOnClass({File.class})
@AutoConfigureAfter(CommonAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = FileRepository.class)
@PropertySource("classpath:/filemanager.properties")
@EntityScan(basePackageClasses = File.class)
public class FileManagerAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(FileContentDao.class)
  public FileContentDao fileContentDao(FileContentRepository fileContentRepository) {
    return new FileContentDaoImpl(fileContentRepository);
  }

  @Bean
  @ConditionalOnMissingBean(DigestUtil.class)
  public DigestUtil digestUtil() {
    return new DigestUtilImpl();
  }

  @Bean
  @ConditionalOnMissingBean(FileService.class)
  public FileService fileService(FileDao fileDao, FileDTOMapper fileDTOMapper, FileContentDao fileContentDao,
      DigestUtil digestUtil, ApplicationEventPublisher eventPublisher) {
    return new FileServiceImpl(fileDao, fileDTOMapper, fileContentDao, digestUtil, eventPublisher);
  }

  @Bean
  @ConditionalOnMissingBean(FileDao.class)
  public FileDao fileDao(FileRepository fileRepository) {
    return new FileDaoImpl(fileRepository);
  }

  @Bean
  @ConditionalOnMissingBean(FileDTOMapper.class)
  public FileDTOMapper fileDTOMapper() {
    return new FileDTOMapper();
  }

  @Bean
  public IndexationEngineImpl<FileDTO> fileIndexationEngine(Client client, FileService fileService,
      BulkProcessor bulkProcessor, ObjectMapper objectMapper,
      @Value("classpath:/elasticsearch/files.json") Resource resource) {
    return new IndexationEngineImpl<>(FileDTO.class, client, resource, "files", u -> "file", fileService,
        bulkProcessor, objectMapper);
  }

  @Bean
  public SearchEngineImpl<FileDTO> fileSearchEngine(Client client, BulkProcessor bulkProcessor,
      ObjectMapper objectMapper) {
    return new SearchEngineImpl<>(FileDTO.class, client, Lists.newArrayList("name", "contentType", "extension"),
        "files", objectMapper);
  }

  @Bean
  @Qualifier("fileIndexationFullJob")
  public JobDetailFactoryBean fileIndexationFullJob() {
    JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
    factoryBean.setJobClass(FileIndexationJob.class);
    factoryBean.setGroup("Indexation");
    factoryBean.setName("Files Indexation Job");
    factoryBean.setDescription("Files full indexation Job");
    factoryBean.setDurability(true);
    return factoryBean;
  }

  @Bean
  @Qualifier("fileScheduledIndexationTrigger")
  public SimpleTriggerFactoryBean fileScheduledIndexationTrigger(
      @Qualifier("fileIndexationFullJob") JobDetail fileIndexationFullJob) {
    SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
    factoryBean.setName("File re-indexation");
    factoryBean.setDescription("Periodic re-indexation of all files of the application");
    factoryBean.setJobDetail(fileIndexationFullJob);
    factoryBean.setStartDelay((long) 30 * 1000);
    factoryBean.setRepeatInterval(1 * 60 * 60 * 1000);
    factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
    return factoryBean;
  }
}
