package process;

import com.softHeart.db.AnswersHolder;
import com.softHeart.db.DbAnswerHolder;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertNotNull;

public class QuestionHandlerTest {

    @Test
    public void answerByThemeTest() throws Exception {
        Connection connection = com.softHeart.db.Connection.getConnection();
        AnswersHolder answersHolder = new DbAnswerHolder(connection);
        String answerByTheme = answersHolder.findByTheme("program").orElse(null);
        System.out.println(answerByTheme);
        assertNotNull(answerByTheme);
    }

}
