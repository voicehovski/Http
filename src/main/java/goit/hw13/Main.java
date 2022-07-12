package goit.hw13;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        performRequest ( new GetUsersListPerformer(  ), "Get all users" );
        performRequest ( new GetUserPerformer( 1 ), "Get user by id" );
        performRequest ( new GetUserByNamePerformer( "Leanne Graham" ), "Get user by name" );
        performRequest ( new DeleteUserPerformer( 1 ), "Delete user by id" );
        performRequest ( new UpdateUserPerformer( User .createRandomUser ( 1 ) ), "Update user by id" );
        performRequest ( new CreateUserPerformer( User .createRandomUser (  ) ), "Create user" );
        performRequest ( new GetUserLastPostCommentsPerformer( 4 ), "User last post comments" );
        performRequest ( new GetUserActiveTodosPerformer( 3 ), "User active todos" );
    }

    public static void performRequest ( ResponsePerformer performer, String title ) {
        System.out.println ( title );
        performer .perform (  );
        System.out.println (  );
    }
}

abstract class ResponsePerformer {

    public abstract HttpRequestBase createRequest (  );

    public abstract void printResult ( String json );

    public void perform(  ) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .build();

        HttpRequestBase request = createRequest (  );

        try(CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .build();
            CloseableHttpResponse response = client.execute(request))
        {

            printStatus ( response );

            HttpEntity entity = response.getEntity();
            String entityAsString = "";
            if (Objects.nonNull(entity)) {
                entityAsString = EntityUtils.toString(entity);
                printResult ( entityAsString );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printStatus ( HttpResponse response ) {
        System.out.println ( response .getStatusLine (  ) .toString (  ) );
    }

    // See https://stackoverflow.com/questions/10067441/gson-converts-to-linkedhashmap-instead-of-my-object
    public <T> List<T> jsonToObjectList ( String jsonString ) {
        Type listElementType = new TypeToken<List<T>>(){} .getType (  );
        Gson gson = new Gson();
        List<T> objects = gson .fromJson ( jsonString, listElementType );
        return objects;
    }
    public List<User> jsonToUserList ( String jsonString ) {
        Type listElementType = new TypeToken<List<User>>(){} .getType (  );
        Gson gson = new Gson();
        return gson .fromJson ( jsonString, listElementType );
    }
    public List<Post> jsonToPostList ( String jsonString ) {
        Type listElementType = new TypeToken<List<Post>>(){} .getType (  );
        Gson gson = new Gson();
        return gson .fromJson ( jsonString, listElementType );
    }
    public List<Comment> jsonToCommentList ( String jsonString ) {
        Type listElementType = new TypeToken<List<Comment>>(){} .getType (  );
        Gson gson = new Gson();
        return gson .fromJson ( jsonString, listElementType );
    }
    public List<Todo> jsonToTodoList ( String jsonString ) {
        Type listElementType = new TypeToken<List<Todo>>(){} .getType (  );
        Gson gson = new Gson();
        return gson .fromJson ( jsonString, listElementType );
    }
}

abstract class SingleUserPreformer extends ResponsePerformer {
    @Override
    public void printResult ( String json ) {
        Gson gson = new Gson();
        User user = gson.fromJson ( json, User .class );
        System.out.println ( user );
    }
}

abstract class UserListPerformer extends ResponsePerformer {
    @Override
    public void printResult ( String json ) {
        List<User> users = jsonToUserList ( json );
        System.out.println ( users );
    }
}

class GetUsersListPerformer extends UserListPerformer {

    @Override
    public HttpRequestBase createRequest() {
        return new HttpGet ("https://jsonplaceholder.typicode.com/users" );
    }
}

class GetUserPerformer extends SingleUserPreformer {

    private int id;

    public GetUserPerformer(int id ) {
        this.id = id;
    }

    @Override
    public HttpRequestBase createRequest (  ) {
        return new HttpGet ("https://jsonplaceholder.typicode.com/users/" + id );
    }
}

class GetUserByNamePerformer extends ResponsePerformer {

    private String name;

    public GetUserByNamePerformer(String name ) {
        this.name = name;
    }

    @Override
    public HttpRequestBase createRequest() {
        return new HttpGet ("https://jsonplaceholder.typicode.com/users/" );
    }

    @Override
    public void printResult ( String json ) {
        /*List<User> result = this.<User>jsonToObjectList ( json ) .stream (  )
                .filter ( u -> name .equals ( u .getName (  ) ) )
                .collect ( Collectors .toList (  ) );*/

        //List<User> users = this.<User>jsonToObjectList ( json );
        List<User> users = jsonToUserList ( json );
        for ( User user : users ) {
            if ( name .equals ( user .getName (  ) ) ) {
                System.out.println ( user );
                break;
            }
        }
    }
}

class DeleteUserPerformer extends ResponsePerformer {

    private int id;

    public DeleteUserPerformer(int id ) {
        this.id = id;
    }

    @Override
    public HttpRequestBase createRequest (  ) {
        return new HttpDelete ( "https://jsonplaceholder.typicode.com/users/" + id );
    }

    @Override
    public void printResult ( String json ) {

    }
}

class UpdateUserPerformer extends SingleUserPreformer {

    private User user;

    public UpdateUserPerformer(User user ) {
        this .user = user;
    }

    @Override
    public HttpRequestBase createRequest (  ) {
        HttpPut request = new HttpPut ("https://jsonplaceholder.typicode.com/users/" + user .getId() );
        String json = new Gson (  ) .toJson ( user );
        try {
            request .setEntity ( new StringEntity ( json ) );
        } catch ( UnsupportedEncodingException uee ) { throw new RuntimeException ( "UnsupportedEncodingException" ); }
        request .setHeader("Content-type", "application/json");
        request .setHeader("charset", "UTF-8");
        return request;
    }
}

class CreateUserPerformer extends SingleUserPreformer {

    private User user;

    public CreateUserPerformer(User user ) {
        this.user = user;
    }

    @Override
    public HttpRequestBase createRequest (  ) {
        HttpPost request =  new HttpPost("https://jsonplaceholder.typicode.com/users/" );
        String json = new Gson (  ) .toJson ( user );
        try {
            request .setEntity ( new StringEntity ( json ) );
        } catch ( UnsupportedEncodingException uee ) { throw new RuntimeException ( "UnsupportedEncodingException" ); }
        request .setHeader("Content-type", "application/json");
        request .setHeader("charset", "UTF-8");
        return request;
    }
}

class GetUserLastPostCommentsPerformer extends ResponsePerformer {

    private long userId;

    public GetUserLastPostCommentsPerformer ( long id ) {
        this.userId = id;
    }

    @Override
    public HttpRequestBase createRequest() {
        return new HttpGet ("https://jsonplaceholder.typicode.com/users/" + userId + "/posts" );
    }

    @Override
    public void printResult ( String json ) {
        Post lastPost = this.<Post>jsonToPostList ( json ) .stream (  )
                .max ( Comparator.comparing ( Post::getUserId))
                .get ();

        ResponsePerformer getPostCommentsPerformer = new GetPostCommentsPerformer ( lastPost .getId (  ), userId );
        getPostCommentsPerformer .perform (  );
    }
}

class GetPostCommentsPerformer extends ResponsePerformer {

    private long postId;
    private long userId;

    public GetPostCommentsPerformer ( long postId, long userId ) {
        this.postId = postId;
        this .userId = userId;
    }

    @Override
    public HttpRequestBase createRequest() {
        return new HttpGet ("https://jsonplaceholder.typicode.com/posts/" + postId + "/comments" );
    }

    @Override
    public void printResult ( String json ) {
        List<Comment> comments = jsonToCommentList ( json );
        System.out.println ( comments );

        FileWriter writer = null;
        try {
            writer = new FileWriter ( String .format ( "user-%d-post-%d-comments.json", userId, postId ) );
            writer .write ( json );
            writer .flush (  );
        } catch ( IOException ioe ) {
            /*Logger .log ( "Can`t write..." )*/
        } finally {
            if ( writer != null ) {
                try {
                    writer .close();
                } catch ( IOException ioe2 ) {
                    throw new RuntimeException ( ioe2 .getMessage (  ) );
                }
            }
        }
    }
}

class GetUserActiveTodosPerformer extends ResponsePerformer {

    private long userId;

    public GetUserActiveTodosPerformer(long userId) {
        this.userId = userId;
    }

    @Override
    public HttpRequestBase createRequest() {
        return new HttpGet ("https://jsonplaceholder.typicode.com/users/" + userId + "/todos" );
    }

    @Override
    public void printResult ( String json ) {
        List<Todo> todos = jsonToTodoList ( json ) .stream (  )
                // .filter ( ! Todo::isCompleted ) А так можно сделать?
                .filter ( t -> ! t .isCompleted (  ) )
                .collect(Collectors.toList());
        System.out.println ( todos );
    }
}