if strcmp(TimeSeriesParam.type,'lorenzNetwork3')
    disclen=floor(TimeSeriesParam.discardTime/TimeSeriesParam.dt/TimeSeriesParam.samplePeriod); %discard length
    len=floor(TimeSeriesParam.useTime/TimeSeriesParam.dt/TimeSeriesParam.samplePeriod); %useful length
    nodeNum=TimeSeriesParam.nodeNum;
    %first disclen elements are discarded.
    %malloc memory
    rawData=zeros(3*nodeNum,disclen+len);
    %randomize seed
    if TimeSeriesParam.noisemode~=0
        rng('shuffle');
    end
    %initial value setting
    if (isfield(TimeSeriesParam,'initialvalue'))
        for i=1:nodeNum
            rawData((3*i-2):(3*i),1)=TimeSeriesParam.initialvalue(:,i);
        end
        clear i
    else
        rawData(1:3,1)=[3; 5.1; 4.2];
        rawData(4:6,1)=[1.32; 3.21; 3.9983];
        rawData(7:9,1)=[0.987321; 1.328; 8.3211];
    end
    % function
    lorenzFoo=@(x,y,z,rho,sigma,beta)...
        ([sigma*(y-x);...
        x*(rho-z)-y;...
        x*y-beta*z ]);
    nowState=zeros(3*nodeNum,1);
    newState=nowState;
    tmpState=newState;
    %generate time series
    for t=1:len+disclen-1
        nowState=rawData(:,t);
        newState=nowState;
        
        for j=1:TimeSeriesParam.samplePeriod
            for k=1:nodeNum
                % Runge Kutta
                % https://en.wikipedia.org/wiki/Runge%E2%80%93Kutta_methods
                k1=TimeSeriesParam.dt*lorenzFoo(nowState(3*k-2),nowState(3*k-1),nowState(3*k),...
                    TimeSeriesParam.rho(k),TimeSeriesParam.sigma(k),TimeSeriesParam.beta(k));
                k2=TimeSeriesParam.dt*lorenzFoo(nowState(3*k-2)+k1(1)/2,nowState(3*k-1)+k1(2)/2,nowState(3*k)+k1(3)/2,...
                    TimeSeriesParam.rho(k),TimeSeriesParam.sigma(k),TimeSeriesParam.beta(k));
                k3=TimeSeriesParam.dt*lorenzFoo(nowState(3*k-2)+k2(1)/2,nowState(3*k-1)+k2(2)/2,nowState(3*k)+k2(3)/2,...
                    TimeSeriesParam.rho(k),TimeSeriesParam.sigma(k),TimeSeriesParam.beta(k));
                k4=TimeSeriesParam.dt*lorenzFoo(nowState(3*k-2)+k3(1),nowState(3*k-1)+k3(2),nowState(3*k)+k3(3),...
                    TimeSeriesParam.rho(k),TimeSeriesParam.sigma(k),TimeSeriesParam.beta(k));
                
                tmpState((3*k-2):(3*k))=newState((3*k-2):(3*k))+k1/6+k2*2/6+k3*2/6+k4/6;
                
                
            end
            % couple
            if t>disclen/2
                for k=1:nodeNum
                    for k2=1:nodeNum
                        if k2==k
                            continue;
                        end
                        tmpState(3*k-2)=tmpState(3*k-2)-TimeSeriesParam.dt*nowState(3*k2-2)*TimeSeriesParam.mu(k,k2);
                    end
                end
            end
            
            newState=tmpState;
        end   
        
        % noise, mode=1
        if TimeSeriesParam.noisemode==1
            GaussianNoise=randn(size(newState));
            GaussianNoise=min(TimeSeriesParam.noiseamphlimit,max(-TimeSeriesParam.noiseamphlimit,GaussianNoise));
            GaussianNoise=GaussianNoise*TimeSeriesParam.noiseamph;
            newState=newState+GaussianNoise*TimeSeriesParam.dt;
            %         x(i+1)=x(i+1)+noisemodifier(1);
            %         y(i+1)=y(i+1)+noisemodifier(2);
            %         z(i+1)=z(i+1)+noisemodifier(3);
        end
        
        
        rawData(:,t+1)=newState;
    end
    % noise mode 2
    if TimeSeriesParam.noisemode==2
        GaussianNoise=randn(size(rawData));
        GaussianNoise=min(TimeSeriesParam.noiseamphlimit,max(-TimeSeriesParam.noiseamphlimit,GaussianNoise));
        GaussianNoise=GaussianNoise*TimeSeriesParam.noiseamph;
        rawData=rawData+GaussianNoise;
        %     x=x+TimeSeriesParam.noiseamph*min(max(randn(size(x)),-TimeSeriesParam.noiseamphlimit),TimeSeriesParam.noiseamphlimit);
        %     y=y+TimeSeriesParam.noiseamph*min(max(randn(size(y)),-TimeSeriesParam.noiseamphlimit),TimeSeriesParam.noiseamphlimit);
        %     z=z+TimeSeriesParam.noiseamph*min(max(randn(size(z)),-TimeSeriesParam.noiseamphlimit),TimeSeriesParam.noiseamphlimit);
    end
    %discard the first disclen elements
    rawData=rawData(:,disclen+1:disclen+len);
    TimeSeries=zeros(len,nodeNum);
    for k=1:nodeNum
        TimeSeries(:,k)=rawData(3*k-2,:)';
    end
    
    % clear local variables
    clear disclen len nodeNum noisemodifier j k k2 k1 k2 k3 k4 newState nowState tmpState t rawData
    
    % TimeSeries(t,i): node i 's position at time t
end