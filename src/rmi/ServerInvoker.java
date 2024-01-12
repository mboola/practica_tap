package rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Future;

import core.application.Invokable;
import core.exceptions.NoInvokerAvailable;
import core.exceptions.NoPolicyManagerRegistered;
import core.exceptions.OperationNotValid;
import core.invoker.CompositeInvoker;
import core.invoker.Invoker;
import core.invoker.InvokerInterface;
import policymanager.PolicyManager;


public class ServerInvoker extends UnicastRemoteObject implements InvokerInterface {

	private	Invoker	invoker;

	protected ServerInvoker(Long ram, boolean isComposite) throws RemoteException {
		super();
		if (isComposite)
			invoker = CompositeInvoker.createInvoker(ram, 4);
		else
			invoker = Invoker.createInvoker(ram, 4);
	}

	/**
	 * This gets the data as a String[] to create a invoker (composite or not) and the port it will be hosted.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		ServerInvoker server;
		try {
			server = new ServerInvoker(Long.valueOf(args[1]), Integer.parseInt(args[2]) == 1);

			Registry registry = LocateRegistry.createRegistry(Integer.valueOf(args[0]));

			registry.rebind("Invoker", server);

			System.out.println("Invoker with id " + server.getId() + " created and ready.");
		} catch (Exception e) {
			System.err.println("Excepcion del servidor.");
			e.printStackTrace();
		}
	}

	@Override
	public String getId() throws RemoteException {
		return (invoker.getId());
	}

	@Override
	public long	getUsedRam() throws RemoteException {
		return (invoker.getUsedRam());
	}

	@Override
	public long getAvailableRam() throws RemoteException {
		return (invoker.getAvailableRam());
	}

	@Override
	public void reserveRam(long ram) throws RemoteException {
		invoker.reserveRam(ram);
	}

	@Override
	public long getMaxRam() throws RemoteException {
		return (invoker.getMaxRam());
	}

	@Override
	public <T, R> R invoke(Invokable<T, R> invokable, T args, String id) throws Exception {
		return (invoker.invoke(invokable, args, id));
	}

	@Override
	public <T, R> Future<R> invokeAsync(Invokable<T, R> invokable, T args, String id) throws Exception {
		return (invoker.invokeAsync(invokable, args, id));
	}

	@Override
	public InvokerInterface selectInvoker(long ram) throws NoPolicyManagerRegistered, NoInvokerAvailable, RemoteException {
		return (invoker.selectInvoker(ram));
	}

	@Override
	public void	setPolicyManager(PolicyManager policyManager) throws RemoteException {
		invoker.setPolicyManager(policyManager);
	}

	@Override
	public void shutdownInvoker() throws RemoteException {
		invoker.shutdownInvoker();
	}

	@Override
	public void registerInvoker(InvokerInterface invoker) throws OperationNotValid, RemoteException {
		this.invoker.registerInvoker(invoker);
	}

	@Override
	public void deleteInvoker(InvokerInterface invoker) throws OperationNotValid, RemoteException {
		this.invoker.deleteInvoker(invoker);
	}

	@Override
	public void setDistributionPolicyManager(int size, long ram) throws NoInvokerAvailable, RemoteException {
		this.invoker.setDistributionPolicyManager(size, ram);
	}
}
