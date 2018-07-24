import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import static jade.core.behaviours.ParallelBehaviour.WHEN_ALL;

public class Cerebro extends Agent {

    private static int numCarSensorV;
    private static int numCarSensorH;
    private static long tempoSinal;
    private static int contador = 0;

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        if (contador==0){
        this.contador=0;
        }else{
        this.contador = this.contador + contador;
        }
    }

    public long getTempoSinal() {
        return tempoSinal;
    }

    public void setTempoSinal(long tempoSinal) {

        this.tempoSinal = tempoSinal;
    }

    public int getNumCarSensorV() {
        return numCarSensorV;
    }

    public void setNumCarSensorV(int numCarSensorV) {

        this.numCarSensorV = numCarSensorV;
    }

    public int getNumCarSensorH() {

        return numCarSensorH;
    }

    public void setNumCarSensorH(int numCarSensorH) {
        this.numCarSensorH = numCarSensorH;
    }

    protected void setup () {
        //Receber Msg

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    String content = msg.getContent();
                    if ("sensorVertical@127.0.0.1:1099/JADE".equals(msg.getSender().getName())) {
                        //salvar a quantidade e fazer a comparação com o outro sensor pra mandar msg pros semaforos
                        System.out.println("O agente " + msg.getSender().getName() + " avisa "+content+" carros na via vertical");
                        setNumCarSensorV(Integer.parseInt(content));
                        setContador(1);

                    }
                    if ("sensorHorizontal@127.0.0.1:1099/JADE".equals(msg.getSender().getName())) {
                        //salvar a quantidade e fazer a comparação com o outro sensor pra mandar msg pros semaforos
                        System.out.println("O agente " + msg.getSender().getName() + " avisa "+content+" carros na via horizontal");
                        setNumCarSensorH(Integer.parseInt(content));
                        setContador(1);
                    }

                    if(getContador() ==2){

                        if ((getNumCarSensorH()-getNumCarSensorV())<=10){
                            setTempoSinal(30000);
                            System.out.println("Diferença menor que 10 carros. Manter configuração inicial ");
                        }
                        if ((getNumCarSensorH()-getNumCarSensorV())>10 && (getNumCarSensorH()-getNumCarSensorV())<30){
                            setTempoSinal(80000);
                            System.out.println("Diferença entre 10 e 29 carros. Ativar configuração 1 dos semaforos ");
                        }
                        if ((getNumCarSensorH()-getNumCarSensorV())>=30 && (getNumCarSensorH()-getNumCarSensorV())<50){
                            setTempoSinal(100000);
                            System.out.println("Diferença entre 30 e 49 carros. Ativar configuração 2 dos semaforos ");
                        }

                        if ((getNumCarSensorH()-getNumCarSensorV())>=50){
                            setTempoSinal(150000);
                            System.out.println("Diferença maior que 50 carros. Ativar configuração 3 dos semaforos ");
                        }


                        String tempoSinalS = Long.toString(getTempoSinal());


                        if(getNumCarSensorH()>=getNumCarSensorV()){
                            ACLMessage msg1 = new ACLMessage(ACLMessage.INFORM);
                            msg1.addReceiver(new AID("sinalHorizontal",AID.ISLOCALNAME));
                            msg1.setLanguage("Português");
                            msg1.setOntology("+verde-vermelho");
                            msg1.setContent(tempoSinalS);
                            myAgent.send(msg1);


                            ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
                            msg2.addReceiver(new AID("sinalVertical",AID.ISLOCALNAME));
                            msg2.setLanguage("Português");
                            msg2.setOntology("-verde+vermelho");
                            msg2.setContent(tempoSinalS);
                            myAgent.send(msg2);

                        }
                        else{
                            ACLMessage msg1 = new ACLMessage(ACLMessage.INFORM);
                            msg1.addReceiver(new AID("sinalHorizontal",AID.ISLOCALNAME));
                            msg1.setLanguage("Português");
                            msg1.setOntology("-verde+vermelho");
                            msg1.setContent(tempoSinalS);
                            myAgent.send(msg1);


                            ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
                            msg2.addReceiver(new AID("sinalVertical",AID.ISLOCALNAME));
                            msg2.setLanguage("Português");
                            msg2.setOntology("+verde-vermelho");
                            msg2.setContent(tempoSinalS);
                            myAgent.send(msg2);


                        }

                        setContador(0);

                    }

                } else
                    block();
            }


        });
        //Final recepção de MSG
    }
}


