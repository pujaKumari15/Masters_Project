package com.master.project.dto;

import com.master.project.enums.MqttCaller;

public class MqttMessageDto {
		private MqttCaller caller;
		private String message;

		public MqttMessageDto(MqttCaller caller, String message) {
				this.caller = caller;
				this.message = message;
		}

		public MqttCaller getCaller() {
				return caller;
		}

		public void setCaller(MqttCaller caller) {
				this.caller = caller;
		}

		public String getMessage() {
				return message;
		}

		public void setMessage(String message) {
				this.message = message;
		}
}
