import javax.swing.JFrame;
import java.io.IOException;

public class Principal{

        public static void main(String[] args) {

                String[] parametros = {
                        "-gui",
                        "-local-host",
                        "127.0.0.1"
                };
                jade.Boot.main(parametros);

                String[] novoContainer = {
                        "-local-host", "127.0.0.1",
                        "-container", "-container-name",
                        "Meu-Cruzamento","interface:Interface();"
                        +"sinalHorizontal:Semaforo();"
                                + "sinalVertical:Semaforo();"
                                + "sensorVertical:Sensor();"
                                + "sensorHorizontal:Sensor();"
                                + "cerebro:Cerebro()"
                };
                jade.Boot.main(novoContainer);

        }

}
