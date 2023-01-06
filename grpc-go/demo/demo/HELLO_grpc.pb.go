// Code generated by protoc-gen-go-grpc. DO NOT EDIT.

package demo

import (
	context "context"
	grpc "google.golang.org/grpc"
	codes "google.golang.org/grpc/codes"
	status "google.golang.org/grpc/status"
)

// This is a compile-time assertion to ensure that this generated file
// is compatible with the grpc package it is being compiled against.
// Requires gRPC-Go v1.32.0 or later.
const _ = grpc.SupportPackageIsVersion7

// HELLOClient is the client API for HELLO service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://pkg.go.dev/google.golang.org/grpc/?tab=doc#ClientConn.NewStream.
type HELLOClient interface {
	Heathcheck(ctx context.Context, in *HealthcheckRequest, opts ...grpc.CallOption) (*HealthcheckResponse, error)
	Helloworld(ctx context.Context, in *HelloRequest, opts ...grpc.CallOption) (*HelloReply, error)
}

type hELLOClient struct {
	cc grpc.ClientConnInterface
}

func NewHELLOClient(cc grpc.ClientConnInterface) HELLOClient {
	return &hELLOClient{cc}
}

func (c *hELLOClient) Heathcheck(ctx context.Context, in *HealthcheckRequest, opts ...grpc.CallOption) (*HealthcheckResponse, error) {
	out := new(HealthcheckResponse)
	err := c.cc.Invoke(ctx, "/demo.demo.HELLO/Heathcheck", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *hELLOClient) Helloworld(ctx context.Context, in *HelloRequest, opts ...grpc.CallOption) (*HelloReply, error) {
	out := new(HelloReply)
	err := c.cc.Invoke(ctx, "/demo.demo.HELLO/Helloworld", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

// HELLOServer is the server API for HELLO service.
// All implementations must embed UnimplementedHELLOServer
// for forward compatibility
type HELLOServer interface {
	Heathcheck(context.Context, *HealthcheckRequest) (*HealthcheckResponse, error)
	Helloworld(context.Context, *HelloRequest) (*HelloReply, error)
	mustEmbedUnimplementedHELLOServer()
}

// UnimplementedHELLOServer must be embedded to have forward compatible implementations.
type UnimplementedHELLOServer struct {
}

func (UnimplementedHELLOServer) Heathcheck(context.Context, *HealthcheckRequest) (*HealthcheckResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Heathcheck not implemented")
}
func (UnimplementedHELLOServer) Helloworld(context.Context, *HelloRequest) (*HelloReply, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Helloworld not implemented")
}
func (UnimplementedHELLOServer) mustEmbedUnimplementedHELLOServer() {}

// UnsafeHELLOServer may be embedded to opt out of forward compatibility for this service.
// Use of this interface is not recommended, as added methods to HELLOServer will
// result in compilation errors.
type UnsafeHELLOServer interface {
	mustEmbedUnimplementedHELLOServer()
}

func RegisterHELLOServer(s grpc.ServiceRegistrar, srv HELLOServer) {
	s.RegisterService(&HELLO_ServiceDesc, srv)
}

func _HELLO_Heathcheck_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(HealthcheckRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(HELLOServer).Heathcheck(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/demo.demo.HELLO/Heathcheck",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(HELLOServer).Heathcheck(ctx, req.(*HealthcheckRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _HELLO_Helloworld_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(HelloRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(HELLOServer).Helloworld(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/demo.demo.HELLO/Helloworld",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(HELLOServer).Helloworld(ctx, req.(*HelloRequest))
	}
	return interceptor(ctx, in, info, handler)
}

// HELLO_ServiceDesc is the grpc.ServiceDesc for HELLO service.
// It's only intended for direct use with grpc.RegisterService,
// and not to be introspected or modified (even as a copy)
var HELLO_ServiceDesc = grpc.ServiceDesc{
	ServiceName: "demo.demo.HELLO",
	HandlerType: (*HELLOServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "Heathcheck",
			Handler:    _HELLO_Heathcheck_Handler,
		},
		{
			MethodName: "Helloworld",
			Handler:    _HELLO_Helloworld_Handler,
		},
	},
	Streams:  []grpc.StreamDesc{},
	Metadata: "HELLO.proto",
}