package com.oficina.sistemapecas;

import com.oficina.sistemapecas.ui.MainWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SistemaPecasApplication {

	public static void main(String[] args) {



		ConfigurableApplicationContext context = SpringApplication.run(SistemaPecasApplication.class);

		javax.swing.SwingUtilities.invokeLater(() -> {
			MainWindow mainWindow = context.getBean(MainWindow.class);
			mainWindow.exibir();
		});

	}

}