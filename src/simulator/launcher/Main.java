package simulator.launcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.control.Controller;
import simulator.events.Event;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.model.DequeuingStrategy;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;


public class Main {

	private final static Integer _timeLimitDefaultValue = 10;
	private final static String _modeDefaultValue = "gui";
	private static String _inFile = null;
	private static String _outFile = null;
	private static Factory<Event> _eventsFactory = null;
	private static Integer _timeLimit = null;
	private static String _mode;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseModeOption(line);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseTicksOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc(
				"Gui mode or console mode").build());
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc(
				"Events input file").build());
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc(
				"Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc(
				"Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc(
				"Ticks to the simulator�s main loop (default\n" + "value is 10).").build());

		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null && _mode == "console") {
			throw new ParseException("An events file is missing");
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		if (_mode.equals("console"))
			_outFile = line.getOptionValue("o");
	}

	private static void parseTicksOption(CommandLine line) throws ParseException {
		try {
			String timeLimitStr = line.getOptionValue("t");

			if(timeLimitStr == null) {
				_timeLimit = _timeLimitDefaultValue;
			} else {
				_timeLimit = Integer.parseInt(timeLimitStr);
			}
		} catch (NumberFormatException e){
			throw new ParseException("ticks option needs an Integer");
		}
	}

	private static void parseModeOption(CommandLine line) throws ParseException {
		String mode = line.getOptionValue("m");

		if(mode.equals("gui") && mode.contentEquals("console")) {
			_mode = _modeDefaultValue; 
		} else {
			_mode = mode;
		}
	}

	private static void initFactories() {
		List<Builder<Event>> ebs = new ArrayList<Builder<Event>>();

		//Factories para LightSwitchingStrategy
		List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add( new RoundRobinStrategyBuilder() );
		lsbs.add( new MostCrowdedStrategyBuilder() );
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory
				<>(lsbs);

		//Factories para DequeuingStrategy
		List<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add( new MoveFirstStrategyBuilder() );
		dqbs.add( new MoveAllStrategyBuilder() );
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(
				dqbs);

		ebs.add( new NewJunctionEventBuilder(lssFactory,dqsFactory) );
		ebs.add( new NewCityRoadEventBuilder() );
		ebs.add( new NewInterCityRoadEventBuilder() );
		ebs.add( new NewVehicleEventBuilder() );
		ebs.add( new SetWeatherEventBuilder() );
		ebs.add( new SetContClassEventBuilder() );


		_eventsFactory = new BuilderBasedFactory<Event>(ebs);

	}

	private static void startBatchMode() throws IOException {
		TrafficSimulator sim = new TrafficSimulator();
		try {
			Controller cont = new Controller(sim, _eventsFactory);

			OutputStream out;
			if(_outFile != null) {
				out = new FileOutputStream(_outFile);
			} else {
				out = System.out;
			}
			File infile = new File(_inFile);
			cont.loadEvents(infile);
			cont.run(_timeLimit, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void startGuiMode() {
		TrafficSimulator sim = new TrafficSimulator();
		try {
			Controller cont = new Controller(sim, _eventsFactory);
			if(_inFile != null) {
				File infile = new File(_inFile);
				cont.loadEvents(infile);
			}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new MainWindow(cont);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void start(String[] args) throws IOException {
		initFactories();
		parseArgs(args);
		if(_mode.equals("console")) {
			startBatchMode();
		} else if (_mode.equals("gui")) {
			startGuiMode();
		}
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
