package com.stratio.anescobar.mafia.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class GenericDto  implements Serializable {

	private static final long serialVersionUID = -7577381570382865330L;

	@SuppressWarnings("unchecked")
	public <T> T deepClone() {
		
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
				oos.writeObject(this);
				ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
				ObjectInputStream ois = new ObjectInputStream(bais);
				return (T) ois.readObject();
			}
		} catch (IOException | ClassNotFoundException e) {
			return null;
		}
	}
}
