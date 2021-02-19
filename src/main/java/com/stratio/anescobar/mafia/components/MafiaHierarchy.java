package com.stratio.anescobar.mafia.components;

public interface MafiaHierarchy {

	String getImplementation();

	public enum MAFIOSOS_STATUS {
		FREE(0), ENCARCELADO(1), ASSESINADO(2);

		private int code;

		MAFIOSOS_STATUS(int code) {
			this.code = code;
		}

		public int code() {
			return code;
		}
	}

}
