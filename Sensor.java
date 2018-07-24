import jade . core . Agent ;
import jade.core.behaviours.*;
import jade . lang . acl . ACLMessage ;
import jade . core .AID;
import java.util.Scanner;

public class Sensor extends Agent {

    private long tempoC = System.currentTimeMillis();
    private String qtdCarros = "50";

    public long getTempoC()
    {
        return tempoC;
    }

    public void setTempoC(long tempoC) {
        this.tempoC = tempoC;
    }

    public String getQtdCarros() {
        return qtdCarros;
    }

    public void setQtdCarros(String qtdCarros) {

        this.qtdCarros = qtdCarros;
    }

    protected void setup() {
        addBehaviour(new CyclicBehaviour(this) {
            public void action () {
                if (System.currentTimeMillis() - getTempoC()  > 15000 ){
                    Scanner ler = new Scanner(System.in);
                    System.out.println("Quantos carros o "+ myAgent.getName()+ "detectou");
                    setQtdCarros(ler.next());
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.addReceiver(new AID("cerebro", AID.ISLOCALNAME));
                    msg.setLanguage("Português");
                    msg.setOntology("Emergência");
                    msg.setContent(getQtdCarros());
                    myAgent.send(msg);
                    setTempoC(System.currentTimeMillis()+30000);
                }
            }
        });
    }
}
