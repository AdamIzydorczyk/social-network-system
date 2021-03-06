package tk.aizydorczyk.sns.operation.domain.post.create

import org.junit.Before
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import tk.aizydorczyk.sns.common.infrastructure.mapper.Mapper
import tk.aizydorczyk.sns.operation.domain.post.Post
import tk.aizydorczyk.sns.operation.domain.post.PostDto
import tk.aizydorczyk.sns.operation.domain.post.PostMapper
import tk.aizydorczyk.sns.operation.domain.support.HashMapRepository
import tk.aizydorczyk.sns.operation.infrastructure.rest.AuditingInformation

import java.time.LocalDateTime
import java.time.ZoneOffset

class PostCreateCommandTest extends Specification {

    @Shared
    private Mapper mapper

    @Before
    void init() {
        this.mapper = new PostMapper()
    }

    @Test
    def "should create new Post from PostDto"() {
        given: "send PostDto with actual time with executing user uuid"
        final PostDto postDto = new PostDto("NEW")
        final LocalDateTime testTime = LocalDateTime.of(12, 12, 12, 12, 12, 12)
        final UUID testUserUUid = UUID.fromString("f734bb20-91cb-4925-92ba-93aab2d19a21")
        final HashMapRepository<Post> postRepository = prepareRepository(testTime, testUserUUid)
        final CreatePostCommand createPostCommand = new CreatePostCommand(postRepository.&save, { post -> mapper.map(post, PostDto.class) }, mapper)
        when: "CreatePostCommand was executed"
        final PostDto result = createPostCommand.execute(postDto, new AuditingInformation(testUserUUid, testTime))

        then: "receive PostDto with assigned sended content, new id and auditing information"
        with(result) {
            getId() == 2L
            getContent() == "NEW"
            getCreatedAt() == testTime.toEpochSecond(ZoneOffset.UTC)
            getCreatedBy() == testUserUUid
        }
    }

    private static HashMapRepository prepareRepository(LocalDateTime testTime, UUID testUserUUid) {
        final def listOfPosts = List.of(new Post.Builder().content("TEST").build())
        return new HashMapRepository<>(listOfPosts, new AuditingInformation(testUserUUid, testTime))
    }

}
