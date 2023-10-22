import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

public class Controller {
	private List<Invoker> invokers;
	private Map<String, Object> actions;

	private ExecutorService executor 
      = Executors.newFixedThreadPool(3);
    
	public static Controller instantiate()
	{
		if (unicInstance == null)
			unicInstance = new Controller();
		return (unicInstance);
	}
	protected Controller() {
		invokers = new LinkedList<Invoker>();
		actions = new HashMap<String, Object>();
	}
	private static Controller unicInstance = null;

	/* Here we will call a function from the proxy */
	private String GetId(Action<Object, Object> action)
	{
		return ("hola:)");
	}

	/* Used to search if we already have this action in our map */
	private boolean hasMapAction(String id)
	{
		if ( actions.isEmpty() )
			return (false);
		if ( actions.get(id) == null )
			return (false);
		return (true);
	}

	public void registerAction(String id, Object f)
	{
		//String	id;

		//id = GetId(action);
		if ( !hasMapAction(id) )
		{
			actions.put(id, f);
			return ;
		}
		//throw error. already exists
	}

	public void	listActions()
	{
		if ( actions.isEmpty())
		{
			//nothing to show
			return ;
		}
		for(String key : actions.keySet())
			System.out.println(key);
	}

	public <T, R> R invokeAction(String id, T args) throws Exception
	{
		Function<T, R>	action;

		if ( !hasMapAction(id) )
		{
			//error, we dont have this action in our map
			System.out.println("Error");
			return (null);
		}
		action = (Function<T, R>)actions.get(id);
		return (action.apply(args));
	}

	public <T, R> List<R> invokeListActions(String id, List<T> args) throws Exception
	{
		Function<T, R>	action;
		List<R> 		result;

		if ( !hasMapAction(id) )
		{
			//error, we dont have this action in our map
			System.out.println("Error");
			return (null);
		}
		result = new LinkedList<R>();
		action = (Function<T, R>)actions.get(id);
		for (T element : args)
			result.add(action.apply(element));
		return (result);
	}

	public <T, R> Future<R> invokeAsyncAction(String id, T args)
	{
		Function<T, R>	action;

		action = (Function<T, R>)actions.get(id);
		return executor.submit( () -> {
			return (action.apply(args));
		});
	}

	public void removeAction(String id)
	{
		if ( !hasMapAction(id) )
		{
			//error, we dont have this action in our map
			return ;
		}
		actions.remove(id);
	}
}