package ru.uip.contract.generator;

public class SpecWriter {

    private final String outputDir;

    public SpecWriter(String outputDir) {
        this.outputDir = outputDir;
    }

    public void write() {
        System.out.println(outputDir);
    }
}
