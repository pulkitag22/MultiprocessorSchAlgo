
--! TRACE

start_section :

		n : integer;
		i : integer;
		absolute_deadline : array (tasks_range) of integer;
		laxity : array (tasks_range) of integer;
		to_run : integer;
		dummy_priority : integer;
		alpha : integer := 3;
		
end section;

priority_section :
		
		
		for i in tasks_range loop
			if (tasks.para(i) = 1) then 
				tasks.suspended(i) := true;
			end if;
		end loop;
		
		for i in tasks_range loop 
			if (tasks.cores(i) = 0) then 
				tasks.suspended(i) := false;
				tasks.para(i) := 0;
				tasks.cores(i) := 2;
			end if;
		end loop;
		
		for i in tasks_range loop
			tasks.cores(i) := tasks.cores(i) - 1;
		end loop;

		-------------------------------------------------------------x----------------------------------------------------------------------	
		for i in tasks_range loop 
			if ((tasks.ready(i) = true) and (tasks.rest_of_capacity(i) > 0)) then
				absolute_deadline(i) := tasks.start_time(i) + (tasks.period(i)*(tasks.activation_number(i) - 1)) + tasks.deadline(i);
				laxity(i) := absolute_deadline(i) - tasks.rest_of_capacity(i) - simulation_time;
				tasks.priority(i) := (laxity(i) / alpha) + 1;
			end if;
		end loop;
		--------------------------------------------------------------x--------------------------------------------------------------------		
		
		dummy_priority := integer'last;
		
		for i in tasks_range loop 
			if ((tasks.ready(i) = true) and (tasks.rest_of_capacity(i) > 0) and (tasks.suspended(i) = false)) then 
				if (dummy_priority > tasks.priority(i)) then 
					dummy_priority := tasks.priority(i);
					to_run := i;
				end if;
			end if;
		end loop;			
		
		tasks.para(to_run) := 1;
		
end section;

election_section :
	
		return to_run;
	
end section;